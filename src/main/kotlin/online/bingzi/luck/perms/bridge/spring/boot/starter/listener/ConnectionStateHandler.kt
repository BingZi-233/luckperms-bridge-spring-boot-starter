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
 * 该类负责处理和管理SSE（Server-Sent Events）连接的状态变化。
 * 主要功能包括：
 * 1. 记录连接状态变化的日志
 * 2. 发布连接状态事件
 * 3. 提供连接状态的追踪
 * 4. 管理连接重试策略
 *
 * @property eventPublisher Spring事件发布器，用于发布连接状态事件
 * @property connectionManager SSE连接管理器，负责管理连接状态
 * @property retryStrategy SSE重试策略，定义连接重试的策略和规则
 * @property retryListener SSE重试监听器，处理重试相关的事件
 * @property okHttpClient SSE客户端，用于发起HTTP请求
 */
@Component
class ConnectionStateHandler(
    private val eventPublisher: ApplicationEventPublisher,
    private val connectionManager: SSEConnectionManager,
    private val retryStrategy: SSERetryStrategy,
    private val retryListener: SSERetryListener,
    @Qualifier("sseOkHttpClient") private val okHttpClient: OkHttpClient
) : SmartLifecycle, ConnectionStateProcessor {
    private val logger = LoggerFactory.getLogger(ConnectionStateHandler::class.java) // 日志记录器
    private var running = false // 表示当前处理器是否在运行状态
    private val taskExecutor: ThreadPoolTaskExecutor by lazy {
        // 创建一个线程池任务执行器
        ThreadPoolTaskExecutor().apply {
            corePoolSize = 1 // 核心线程数
            maxPoolSize = 1 // 最大线程数
            setThreadNamePrefix("connection-state-handler-") // 线程名称前缀
            initialize() // 初始化线程池
        }
    }

    // 存储每个端点对应的EventSource
    private val eventSources = mutableMapOf<String, EventSource>()

    /**
     * 初始化方法，在Bean创建后执行
     * 设置当前处理器为运行状态，并记录启动日志
     */
    @PostConstruct
    fun init() {
        running = true
        logger.info("LuckPerms Bridge 事件监听器已启动")
    }

    /**
     * 销毁方法，在Bean销毁时执行
     * 设置当前处理器为非运行状态，关闭所有连接并清理资源
     */
    @PreDestroy
    fun destroy() {
        running = false
        // 关闭所有连接
        eventSources.forEach { (_, source) ->
            try {
                source.cancel() // 取消EventSource连接
            } catch (e: Exception) {
                logger.warn("关闭SSE连接时发生错误: {}", e.message) // 记录警告日志
            }
        }
        eventSources.clear() // 清空连接映射
        taskExecutor.shutdown() // 关闭线程池
        logger.info("LuckPerms Bridge 事件监听器已关闭")
    }

    override fun start() {
        running = true // 设置为运行状态
    }

    override fun stop() {
        running = false // 设置为非运行状态
        taskExecutor.shutdown() // 关闭线程池
    }

    override fun isRunning(): Boolean = running // 返回当前运行状态

    override fun getPhase(): Int = Int.MAX_VALUE // 返回生命周期阶段

    override fun isAutoStartup(): Boolean = true // 返回是否自动启动

    /**
     * 处理连接建立事件
     *
     * @param eventSource SSE事件源，表示建立的SSE连接
     * @param response HTTP响应对象，包含连接响应信息
     * @param endpoint 连接的目标端点，表示SSE连接的地址
     */
    override fun handleConnectionOpen(eventSource: EventSource, response: Response, endpoint: String) {
        if (!running) { // 如果当前处理器未运行，记录调试日志并返回
            logger.debug("系统正在关闭，不处理SSE连接建立事件")
            return
        }
        eventSources[endpoint] = eventSource // 存储当前连接的EventSource
        connectionManager.updateState(endpoint, ConnectionStateType.CONNECTED) // 更新连接状态为已连接
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionStateType.CONNECTED,
            endpoint = endpoint,
            message = "SSE连接已建立" // 发布连接建立事件
        )
    }

    /**
     * 处理连接关闭事件
     *
     * @param eventSource SSE事件源，表示关闭的SSE连接
     * @param endpoint 连接的目标端点，表示SSE连接的地址
     */
    override fun handleConnectionClosed(eventSource: EventSource, endpoint: String) {
        if (!running) { // 如果当前处理器未运行，记录调试日志并返回
            logger.debug("系统正在关闭，不处理SSE连接关闭事件")
            return
        }
        eventSources.remove(endpoint) // 移除关闭的连接
        connectionManager.updateState(endpoint, ConnectionStateType.CLOSED) // 更新连接状态为已关闭
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionStateType.CLOSED,
            endpoint = endpoint,
            message = "SSE连接已关闭" // 发布连接关闭事件
        )

        // 触发重连机制
        initiateRetry(eventSource, endpoint, null) // 尝试重连
    }

    /**
     * 处理连接失败事件
     *
     * @param eventSource SSE事件源，表示失败的SSE连接
     * @param endpoint 连接的目标端点，表示SSE连接的地址
     * @param error 导致失败的异常对象，表示连接失败的原因
     */
    override fun handleConnectionFailure(eventSource: EventSource, endpoint: String, error: Throwable?) {
        if (!running) { // 如果当前处理器未运行
            // 检查是否为Socket关闭异常，如果是则记录调试日志
            if (error is SocketException && error.message?.contains("Socket closed") == true) {
                logger.debug("系统正在关闭，SSE连接正常关闭")
            } else {
                logger.debug("系统正在关闭，不处理SSE连接失败事件")
            }
            return
        }

        // 触发重连机制
        initiateRetry(eventSource, endpoint, error) // 尝试重连
    }

    /**
     * 启动重试机制
     *
     * @param eventSource SSE事件源，表示需要重试的SSE连接
     * @param endpoint 连接的目标端点，表示SSE连接的地址
     * @param error 可选的错误信息，表示连接失败的原因
     */
    private fun initiateRetry(eventSource: EventSource, endpoint: String, error: Throwable?) {
        // 检查是否应该重试
        if (retryStrategy.shouldRetry(error)) {
            val retryCount = connectionManager.getRetryCount(endpoint) // 获取当前重试次数
            if (retryCount < retryStrategy.getMaxAttempts()) { // 检查是否超过最大重试次数
                val backoffPeriod = retryStrategy.getBackoffPeriod(retryCount) // 计算退避时间
                connectionManager.updateState(endpoint, ConnectionStateType.RETRYING) // 更新状态为重试中
                
                // 使用Spring Retry模板进行重试
                val retryTemplate = retryStrategy.createRetryTemplate()
                taskExecutor.execute { // 在任务执行器中执行重试逻辑
                    try {
                        retryTemplate.execute<Unit, Throwable>({ context ->
                            // 设置重试上下文的端点信息
                            retryListener.setEndpoint(context, endpoint)
                            // 更新状态为正在连接
                            connectionManager.updateState(endpoint, ConnectionStateType.CONNECTING)
                            
                            // 关闭旧的连接
                            eventSources[endpoint]?.cancel() // 取消旧的EventSource连接
                            eventSources.remove(endpoint) // 移除旧的连接
                            
                            // 触发重新连接事件
                            publishConnectionStateEvent(
                                eventSource = eventSource,
                                state = ConnectionStateType.CONNECTING,
                                endpoint = endpoint,
                                message = "正在重新建立SSE连接" // 发布重连事件
                            )
                            
                            // 等待退避时间
                            Thread.sleep(backoffPeriod) // 休眠，等待重试时间

                            // 发布重连事件
                            eventPublisher.publishEvent(
                                SSERetryEvent(
                                    source = eventSource,
                                    endpoint = endpoint,
                                    retryCount = retryCount // 发布重试事件
                                )
                            )

                            // 等待一段时间，确保旧连接完全关闭
                            Thread.sleep(1000) // 休眠，确保旧连接关闭

                            // 创建新的EventSource
                            val request = Request.Builder()
                                .url(endpoint) // 设置请求URL
                                .header("Accept", "text/event-stream") // 设置请求头
                                .build()

                            val newEventSource = EventSources.createFactory(okHttpClient)
                                .newEventSource(request, createListener(endpoint)) // 创建新的EventSource

                            eventSources[endpoint] = newEventSource // 存储新的EventSource
                            
                            Unit
                        })
                    } catch (e: Exception) {
                        handleFinalFailure(eventSource, endpoint, e) // 处理最终失败
                    }
                }
                return
            }
        }

        handleFinalFailure(eventSource, endpoint, error) // 处理最终失败
    }

    /**
     * 创建事件监听器
     *
     * @param endpoint 连接的目标端点，表示SSE连接的地址
     * @return 返回一个EventSourceListener，用于处理事件
     */
    private fun createListener(endpoint: String): EventSourceListener {
        return object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                handleConnectionOpen(eventSource, response, endpoint) // 处理连接建立事件
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                // 忽略ping事件
                if (type == "ping") {
                    return // 如果是ping事件，直接返回
                }
                logger.debug("收到SSE事件 - 类型: {}, 数据: {}", type, data) // 记录收到的SSE事件
            }

            override fun onClosed(eventSource: EventSource) {
                handleConnectionClosed(eventSource, endpoint) // 处理连接关闭事件
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                handleConnectionFailure(eventSource, endpoint, t) // 处理连接失败事件
            }
        }
    }

    /**
     * 处理最终失败状态
     *
     * @param eventSource SSE事件源，表示失败的SSE连接
     * @param endpoint 连接的目标端点，表示SSE连接的地址
     * @param error 可选的错误信息，表示连接失败的原因
     */
    private fun handleFinalFailure(eventSource: EventSource, endpoint: String, error: Throwable?) {
        eventSources.remove(endpoint) // 移除失败的连接
        connectionManager.updateState(endpoint, ConnectionStateType.FAILED) // 更新状态为失败
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionStateType.FAILED,
            endpoint = endpoint,
            message = "SSE连接失败且无法恢复", // 发布连接失败事件
            error = error // 附带错误信息
        )
    }

    /**
     * 发布连接状态事件
     *
     * @param eventSource SSE事件源，表示当前事件的源
     * @param state 连接状态，表示当前连接的状态
     * @param endpoint 连接的目标端点，表示SSE连接的地址
     * @param message 状态描述信息，表示连接状态的描述
     * @param error 可选的错误信息，表示连接过程中出现的错误
     */
    private fun publishConnectionStateEvent(
        eventSource: EventSource,
        state: ConnectionStateType,
        endpoint: String,
        message: String,
        error: Throwable? = null // 可选的错误信息
    ) {
        if (!running) { // 如果当前处理器未运行，直接返回
            return
        }
        
        val event = ConnectionStateEvent(
            source = eventSource,
            state = state,
            endpoint = endpoint,
            message = message,
            error = error // 创建连接状态事件对象
        )

        taskExecutor.execute { // 在任务执行器中发布事件
            try {
                eventPublisher.publishEvent(event) // 发布连接状态事件
            } catch (e: Exception) {
                logger.error("发布连接状态事件失败: {}", e.message, e) // 记录错误日志
            }
        }
    }
}