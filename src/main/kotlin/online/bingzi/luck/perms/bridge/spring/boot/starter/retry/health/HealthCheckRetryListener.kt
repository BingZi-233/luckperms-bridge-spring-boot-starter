package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener
import org.springframework.stereotype.Component

/**
 * 健康检查重试监听器
 * 本类实现了RetryListener接口，主要用于监控健康检查的重试过程。
 * 它会在每次重试发生错误或重试结束时，记录相关的日志信息。
 */
@Component
class HealthCheckRetryListener(
    private val healthCheckManager: HealthCheckManager // 健康检查管理器，用于获取健康检查统计信息
) : RetryListener {
    private val log = LoggerFactory.getLogger(HealthCheckRetryListener::class.java) // 日志记录器

    /**
     * 当重试发生错误时调用该方法
     * 
     * @param context 当前的重试上下文信息
     * @param callback 执行重试的回调
     * @param throwable 重试过程中发生的异常
     */
    override fun <T, E : Throwable> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        val stats = healthCheckManager.getHealthStats() // 获取当前健康检查统计信息
        // 记录错误信息，包括重试次数、异常类型、异常信息和累计停机时间
        log.error(
            "健康检查失败 - 重试次数: ${stats.retryCount}, " +
            "异常类型: ${throwable.javaClass.simpleName}, " +
            "异常信息: ${throwable.message}, " +
            "累计停机时间: ${stats.downtime}ms"
        )
    }

    /**
     * 当重试结束时调用该方法
     * 
     * @param context 当前的重试上下文信息
     * @param callback 执行重试的回调
     * @param throwable 如果重试失败，则为最后发生的异常；如果重试成功，则为null
     */
    override fun <T, E : Throwable> close(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable?
    ) {
        val stats = healthCheckManager.getHealthStats() // 获取当前健康检查统计信息
        if (throwable != null) {
            // 如果重试失败，记录相关的错误信息
            log.error(
                "健康检查重试失败 - 总重试次数: ${stats.retryCount}, " +
                "最后错误: ${throwable.message}, " +
                "累计停机时间: ${stats.downtime}ms"
            )
        } else {
            // 如果重试成功，记录恢复的信息
            log.info(
                "健康检查恢复 - 总重试次数: ${stats.retryCount}, " +
                "累计运行时间: ${stats.uptime}ms, " +
                "最后响应时间: ${stats.lastResponseTime}ms"
            )
        }
    }

    /**
     * 当开始重试时调用该方法
     * 
     * @param context 当前的重试上下文信息
     * @param callback 执行重试的回调
     * @return 返回true表示允许重试，返回false表示不允许重试
     */
    override fun <T, E : Throwable> open(
        context: RetryContext,
        callback: RetryCallback<T, E>
    ): Boolean {
        // 如果当前重试次数大于0，记录开始重试的信息
        if (context.retryCount > 0) {
            log.info("开始第 ${context.retryCount} 次健康检查重试")
        }
        return true // 返回true表示允许重试
    }
}