package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener
import org.springframework.stereotype.Component

/**
 * SSE专用重试监听器
 * 此类用于监控SSE（Server-Sent Events）连接的重试过程。
 * 它实现了RetryListener接口，并通过记录日志来跟踪连接状态和重试信息。
 */
@Component
class SSERetryListener(
    private val connectionManager: SSEConnectionManager // 连接管理器，用于获取SSE连接的状态信息
) : RetryListener {
    private val log = LoggerFactory.getLogger(SSERetryListener::class.java) // 日志记录器

    companion object {
        private const val ENDPOINT_KEY = "sse.endpoint" // 用于存储SSE连接端点信息的键
    }

    /**
     * 当重试过程中发生错误时调用此方法
     * 
     * @param context 重试上下文，包含重试状态和属性
     * @param callback 重试回调，包含重试逻辑
     * @param throwable 引发错误的异常
     */
    override fun <T, E : Throwable> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        val endpoint = context.getAttribute(ENDPOINT_KEY) as? String ?: return // 获取SSE连接的端点，如果没有则返回
        val stats = connectionManager.getConnectionStats(endpoint) // 获取连接统计信息

        log.warn(
            "SSE连接重试失败 - 端点: {}, 重试次数: {}, 错误: {}", 
            endpoint,
            stats.retryCount,
            throwable.message
        )
    }

    /**
     * 在每次重试之前调用此方法
     * 
     * @param context 重试上下文，包含重试状态和属性
     * @param callback 重试回调，包含重试逻辑
     */
    override fun <T, E : Throwable> open(
        context: RetryContext,
        callback: RetryCallback<T, E>
    ): Boolean {
        val endpoint = context.getAttribute(ENDPOINT_KEY) as? String ?: return true // 获取SSE连接的端点，如果没有则返回true
        val stats = connectionManager.getConnectionStats(endpoint) // 获取连接统计信息

        log.info(
            "准备重试SSE连接 - 端点: {}, 重试次数: {}, 当前状态: {}", 
            endpoint,
            stats.retryCount,
            stats.currentState
        )
        return true // 返回true表示允许重试
    }

    /**
     * 在每次重试之后调用此方法
     * 
     * @param context 重试上下文，包含重试状态和属性
     * @param callback 重试回调，包含重试逻辑
     * @param throwable 引发重试的异常
     */
    override fun <T, E : Throwable> close(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable?
    ) {
        val endpoint = context.getAttribute(ENDPOINT_KEY) as? String ?: return // 获取SSE连接的端点，如果没有则返回
        val stats = connectionManager.getConnectionStats(endpoint) // 获取连接统计信息

        log.info(
            "SSE连接重试完成 - 端点: {}, 重试次数: {}, 当前状态: {}", 
            endpoint,
            stats.retryCount,
            stats.currentState
        )
    }

    /**
     * 设置重试上下文的端点信息
     * 
     * @param context 重试上下文，包含重试状态和属性
     * @param endpoint 要设置的SSE连接端点
     */
    fun setEndpoint(context: RetryContext, endpoint: String) {
        context.setAttribute(ENDPOINT_KEY, endpoint) // 在上下文中设置端点信息
    }
}