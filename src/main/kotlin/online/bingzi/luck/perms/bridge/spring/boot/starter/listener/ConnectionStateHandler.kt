package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import okhttp3.Response
import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.ConnectionStateEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.state.ConnectionState
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.SmartLifecycle
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.net.SocketException
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy

/**
 * SSE连接状态处理器
 *
 * 负责处理和管理SSE（Server-Sent Events）连接的状态变化。
 * 主要功能包括：
 * 1. 记录连接状态变化的日志
 * 2. 发布连接状态事件
 * 3. 提供连接状态的追踪
 *
 * @property eventPublisher Spring事件发布器
 */
@Component
class ConnectionStateHandler(
    private val eventPublisher: ApplicationEventPublisher
) : SmartLifecycle {
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

    @PostConstruct
    fun init() {
        running = true
        logger.info("LuckPerms Bridge 事件监听器已启动")
    }

    @PreDestroy
    fun destroy() {
        running = false
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
    fun handleConnectionOpen(eventSource: EventSource, response: Response, endpoint: String) {
        if (!running) {
            logger.debug("系统正在关闭，不处理SSE连接建立事件")
            return
        }
        logger.info("SSE连接已建立 - 订阅端点: {}", endpoint)
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionState.CONNECTED,
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
    fun handleConnectionClosed(eventSource: EventSource, endpoint: String) {
        if (!running) {
            logger.debug("系统正在关闭，不处理SSE连接关闭事件")
            return
        }
        logger.info("SSE连接已关闭 - 订阅端点: {}", endpoint)
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionState.CLOSED,
            endpoint = endpoint,
            message = "SSE连接已关闭"
        )
    }

    /**
     * 处理连接失败事件
     *
     * @param eventSource SSE事件源
     * @param endpoint 连接的目标端点
     * @param error 导致失败的异常对象
     */
    fun handleConnectionFailure(eventSource: EventSource, endpoint: String, error: Throwable?) {
        if (!running) {
            if (error is SocketException && error.message?.contains("Socket closed") == true) {
                logger.debug("系统正在关闭，SSE连接正常关闭")
            } else {
                logger.debug("系统正在关闭，不处理SSE连接失败事件")
            }
            return
        }

        logger.error("SSE连接失败 - 订阅端点: {}", endpoint, error)
        publishConnectionStateEvent(
            eventSource = eventSource,
            state = ConnectionState.FAILED,
            endpoint = endpoint,
            message = "SSE连接失败",
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
        state: ConnectionState,
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