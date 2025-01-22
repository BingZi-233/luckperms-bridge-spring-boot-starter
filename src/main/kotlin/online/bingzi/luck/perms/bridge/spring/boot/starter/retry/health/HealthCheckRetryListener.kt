package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener
import org.springframework.stereotype.Component

/**
 * 健康检查重试监听器
 * 用于监控健康检查的重试过程
 */
@Component
class HealthCheckRetryListener(
    private val healthCheckManager: HealthCheckManager
) : RetryListener {
    private val log = LoggerFactory.getLogger(HealthCheckRetryListener::class.java)

    override fun <T, E : Throwable> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        val stats = healthCheckManager.getHealthStats()
        log.error(
            "健康检查失败 - 重试次数: ${stats.retryCount}, " +
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
            val stats = healthCheckManager.getHealthStats()
            log.error(
                "健康检查重试失败 - 总重试次数: ${stats.retryCount}, " +
                "最后错误: ${throwable.message}, " +
                "累计停机时间: ${stats.downtime}ms"
            )
        } else {
            val stats = healthCheckManager.getHealthStats()
            log.info(
                "健康检查恢复 - 总重试次数: ${stats.retryCount}, " +
                "累计运行时间: ${stats.uptime}ms, " +
                "最后响应时间: ${stats.lastResponseTime}ms"
            )
        }
    }

    override fun <T, E : Throwable> open(
        context: RetryContext,
        callback: RetryCallback<T, E>
    ): Boolean {
        if (context.retryCount > 0) {
            log.info("开始第 ${context.retryCount} 次健康检查重试")
        }
        return true // 返回true表示允许重试
    }
} 