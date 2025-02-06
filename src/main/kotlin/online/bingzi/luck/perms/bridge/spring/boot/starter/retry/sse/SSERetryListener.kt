package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener
import org.springframework.stereotype.Component

/**
 * SSE专用重试监听器
 * 用于监控SSE连接的重试过程
 */
@Component
class SSERetryListener(
    private val connectionManager: SSEConnectionManager
) : RetryListener {
    private val log = LoggerFactory.getLogger(SSERetryListener::class.java)

    companion object {
        private const val ENDPOINT_KEY = "sse.endpoint"
    }

    override fun <T, E : Throwable> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        val endpoint = context.getAttribute(ENDPOINT_KEY) as? String ?: return
        val stats = connectionManager.getConnectionStats(endpoint)
        log.error(
            "SSE连接失败 - 订阅端点: {}, 重试次数: {}, 异常类型: {}, 异常信息: {}, 累计停机时间: {}ms, 当前状态: {}", 
            endpoint,
            stats.retryCount,
            throwable.javaClass.simpleName,
            throwable.message,
            stats.downtime,
            stats.currentState
        )
    }

    override fun <T, E : Throwable> close(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable?
    ) {
        val endpoint = context.getAttribute(ENDPOINT_KEY) as? String ?: return
        val stats = connectionManager.getConnectionStats(endpoint)
        if (throwable != null) {
            log.error(
                "SSE连接重试失败 - 订阅端点: {}, 总重试次数: {}, 最后错误: {}, 累计停机时间: {}ms, 当前状态: {}", 
                endpoint,
                stats.retryCount,
                throwable.message,
                stats.downtime,
                stats.currentState
            )
        } else {
            log.info(
                "SSE连接恢复 - 订阅端点: {}, 总重试次数: {}, 累计运行时间: {}ms, 当前状态: {}", 
                endpoint,
                stats.retryCount,
                stats.uptime,
                stats.currentState
            )
        }
    }

    override fun <T, E : Throwable> open(
        context: RetryContext,
        callback: RetryCallback<T, E>
    ): Boolean {
        val endpoint = context.getAttribute(ENDPOINT_KEY) as? String ?: return true
        if (context.retryCount > 0) {
            val stats = connectionManager.getConnectionStats(endpoint)
            log.info(
                "开始第 {} 次SSE连接重试 - 订阅端点: {}, 累计停机时间: {}ms, 当前状态: {}", 
                context.retryCount,
                endpoint,
                stats.downtime,
                stats.currentState
            )
        }
        return true // 返回true表示允许重试
    }

    /**
     * 设置重试上下文的端点信息
     */
    fun setEndpoint(context: RetryContext, endpoint: String) {
        context.setAttribute(ENDPOINT_KEY, endpoint)
    }
} 