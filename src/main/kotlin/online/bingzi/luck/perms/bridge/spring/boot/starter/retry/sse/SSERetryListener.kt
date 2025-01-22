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

    override fun <T, E : Throwable> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        val stats = connectionManager.getConnectionStats()
        log.error(
            "SSE连接失败 - 重试次数: ${stats.retryCount}, " +
            "异常类型: ${throwable.javaClass.simpleName}, " +
            "异常信息: ${throwable.message}, " +
            "累计停机时间: ${stats.downtime}ms"
        )
    }

    override fun <T, E : Throwable> close(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable?
    ) {
        if (throwable != null) {
            val stats = connectionManager.getConnectionStats()
            log.error(
                "SSE连接重试失败 - 总重试次数: ${stats.retryCount}, " +
                "最后错误: ${throwable.message}, " +
                "累计停机时间: ${stats.downtime}ms"
            )
        } else {
            val stats = connectionManager.getConnectionStats()
            log.info(
                "SSE连接恢复 - 总重试次数: ${stats.retryCount}, " +
                "累计运行时间: ${stats.uptime}ms"
            )
        }
    }

    override fun <T, E : Throwable> open(
        context: RetryContext,
        callback: RetryCallback<T, E>
    ): Boolean {
        if (context.retryCount > 0) {
            log.info("开始第 ${context.retryCount} 次重试")
        }
        return true // 返回true表示允许重试
    }
} 