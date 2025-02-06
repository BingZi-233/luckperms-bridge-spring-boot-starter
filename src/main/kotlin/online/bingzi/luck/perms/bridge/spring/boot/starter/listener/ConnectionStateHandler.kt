package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.ConnectionStateEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.SSERetryEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSEConnectionManager
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryStrategy
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.SmartLifecycle
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.net.SocketException
import okhttp3.OkHttpClient
import okhttp3.sse.EventSources
import org.springframework.beans.factory.annotation.Qualifier

/**
 * SSE连接状态处理器
 *
 * 负责处理和管理SSE（Server-Sent Events）连接的状态变化。
 * 主要功能包括：
 * 1. 记录连接状态变化的日志
 * 2. 发布连接状态事件
 * 3. 提供连接状态的追踪
 * 4. 管理连接重试策略
 *
 * @property eventPublisher Spring事件发布器
 * @property connectionManager SSE连接管理器
 * @property retryStrategy SSE重试策略
 * @property retryListener SSE重试监听器
 * @property okHttpClient SSE客户端
 */
@Component
class ConnectionStateHandler(
    private val eventPublisher: ApplicationEventPublisher,
    private val connectionManager: SSEConnectionManager,
    private val retryStrategy: SSERetryStrategy,
    private val retryListener: SSERetryListener,
    @Qualifier("sseOkHttpClient") private val okHttpClient: OkHttpClient
) : SmartLifecycle, ConnectionStateProcessor {
    private val logger = LoggerFactory.getLogger(ConnectionStateHandler::class.java)
    private var running = false
    private val taskExecutor: ThreadPoolTaskExecutor by lazy {
        ThreadPoolTaskExecutor().apply {
            corePoolSize = 1
            maxPoolSize = 1
            setThreadNamePrefix("connection-state-handler-")
            initialize()
        }
    }

    // 存储每个端点对应的EventSource
    private val eventSources = mutableMapOf<String, EventSource>()

    @PostConstruct
    fun init() {
        running = true
        logger.info("LuckPerms Bridge 事件监听器已启动")
    }

    @PreDestroy
    fun destroy() {
        running = false
        // 关闭所有连接
        eventSources.forEach { (_, source) ->
            try {
                source.cancel()
            } catch (e: Exception) {
                logger.warn("关闭SSE连接时发生错误: {}", e.message)
            }
        }
        eventSources.clear()
        taskExecutor.shutdown()
        logger.info("LuckPerms Bridge 事件监听器已关闭")
    }

    override fun start() {
        running = true
    }

    override fun stop() {
        running = false
        taskExecutor.shutdown()
    }

    override fun isRunning(): Boolean = running

    override fun getPhase(): Int = Int.MAX_VALUE

    override fun isAutoStartup(): Boolean = true

    /**
     * 处理连接建立事件
     *
     * @param eventSource SSE事件源
     * @param response HTTP响应对象
     * @param endpoint 连接的目标端点
     */
    override fun handleConnectionOpen(eventSource: EventSource, response: Response, endpoint: String) {
        if (!running) {
            logger.debug("系统正在关闭，不处理SSE连接建立事件")
            return
        }
        eventSources[endpoint] = eventSource
        connectionManager.updateState(endpoint, ConnectionStateType.CONNECTED)
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionStateType.CONNECTED,
            endpoint = endpoint,
            message = "SSE连接已建立"
        )
    }

    /**
     * 处理连接关闭事件
     *
     * @param eventSource SSE事件源
     * @param endpoint 连接的目标端点
     */
    override fun handleConnectionClosed(eventSource: EventSource, endpoint: String) {
        if (!running) {
            logger.debug("系统正在关闭，不处理SSE连接关闭事件")
            return
        }
        eventSources.remove(endpoint)
        connectionManager.updateState(endpoint, ConnectionStateType.CLOSED)
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionStateType.CLOSED,
            endpoint = endpoint,
            message = "SSE连接已关闭"
        )

        // 触发重连机制
        initiateRetry(eventSource, endpoint, null)
    }

    /**
     * 处理连接失败事件
     *
     * @param eventSource SSE事件源
     * @param endpoint 连接的目标端点
     * @param error 导致失败的异常对象
     */
    override fun handleConnectionFailure(eventSource: EventSource, endpoint: String, error: Throwable?) {
        if (!running) {
            if (error is SocketException && error.message?.contains("Socket closed") == true) {
                logger.debug("系统正在关闭，SSE连接正常关闭")
            } else {
                logger.debug("系统正在关闭，不处理SSE连接失败事件")
            }
            return
        }

        // 触发重连机制
        initiateRetry(eventSource, endpoint, error)
    }

    /**
     * 启动重试机制
     */
    private fun initiateRetry(eventSource: EventSource, endpoint: String, error: Throwable?) {
        // 检查是否应该重试
        if (retryStrategy.shouldRetry(error)) {
            val retryCount = connectionManager.getRetryCount(endpoint)
            if (retryCount < retryStrategy.getMaxAttempts()) {
                val backoffPeriod = retryStrategy.getBackoffPeriod(retryCount)
                connectionManager.updateState(endpoint, ConnectionStateType.RETRYING)
                
                // 使用Spring Retry模板进行重试
                val retryTemplate = retryStrategy.createRetryTemplate()
                taskExecutor.execute {
                    try {
                        retryTemplate.execute<Unit, Throwable>({ context ->
                            // 设置重试上下文的端点信息
                            retryListener.setEndpoint(context, endpoint)
                            // 更新状态为正在连接
                            connectionManager.updateState(endpoint, ConnectionStateType.CONNECTING)
                            
                            // 关闭旧的连接
                            eventSources[endpoint]?.cancel()
                            eventSources.remove(endpoint)
                            
                            // 触发重新连接事件
                            publishConnectionStateEvent(
                                eventSource = eventSource,
                                state = ConnectionStateType.CONNECTING,
                                endpoint = endpoint,
                                message = "正在重新建立SSE连接"
                            )
                            
                            // 等待退避时间
                            Thread.sleep(backoffPeriod)

                            // 发布重连事件
                            eventPublisher.publishEvent(
                                SSERetryEvent(
                                    source = eventSource,
                                    endpoint = endpoint,
                                    retryCount = retryCount
                                )
                            )

                            // 等待一段时间，确保旧连接完全关闭
                            Thread.sleep(1000)

                            // 创建新的EventSource
                            val request = Request.Builder()
                                .url(endpoint)
                                .header("Accept", "text/event-stream")
                                .build()

                            val newEventSource = EventSources.createFactory(okHttpClient)
                                .newEventSource(request, createListener(endpoint))

                            eventSources[endpoint] = newEventSource
                            
                            Unit
                        })
                    } catch (e: Exception) {
                        handleFinalFailure(eventSource, endpoint, e)
                    }
                }
                return
            }
        }

        handleFinalFailure(eventSource, endpoint, error)
    }

    /**
     * 创建事件监听器
     */
    private fun createListener(endpoint: String): EventSourceListener {
        return object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                handleConnectionOpen(eventSource, response, endpoint)
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                // 忽略ping事件
                if (type == "ping") {
                    return
                }
                logger.debug("收到SSE事件 - 类型: {}, 数据: {}", type, data)
            }

            override fun onClosed(eventSource: EventSource) {
                handleConnectionClosed(eventSource, endpoint)
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                handleConnectionFailure(eventSource, endpoint, t)
            }
        }
    }

    /**
     * 处理最终失败状态
     */
    private fun handleFinalFailure(eventSource: EventSource, endpoint: String, error: Throwable?) {
        eventSources.remove(endpoint)
        connectionManager.updateState(endpoint, ConnectionStateType.FAILED)
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionStateType.FAILED,
            endpoint = endpoint,
            message = "SSE连接失败且无法恢复",
            error = error
        )
    }

    /**
     * 发布连接状态事件
     *
     * @param eventSource SSE事件源
     * @param state 连接状态
     * @param endpoint 连接的目标端点
     * @param message 状态描述信息
     * @param error 可选的错误信息
     */
    private fun publishConnectionStateEvent(
        eventSource: EventSource,
        state: ConnectionStateType,
        endpoint: String,
        message: String,
        error: Throwable? = null
    ) {
        if (!running) {
            return
        }
        
        val event = ConnectionStateEvent(
            source = eventSource,
            state = state,
            endpoint = endpoint,
            message = message,
            error = error
        )

        taskExecutor.execute {
            try {
                eventPublisher.publishEvent(event)
            } catch (e: Exception) {
                logger.error("发布连接状态事件失败: {}", e.message, e)
            }
        }
    }
} 