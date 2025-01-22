package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckRetryListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckRetryStrategy
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryStrategy
import org.springframework.retry.support.RetryTemplate

/**
 * 重试模板工厂
 * 用于创建和管理不同类型的重试模板
 */
object RetryTemplateFactory {
    /**
     * 创建固定间隔重试模板
     */
    fun createFixedIntervalRetryTemplate(
        maxAttempts: Int = 3,
        backoffPeriod: Long = 1000,
        retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
            Exception::class.java to true
        )
    ): RetryTemplate {
        val strategy = FixedIntervalRetryStrategy(maxAttempts, backoffPeriod, retryableExceptions)
        val template = strategy.createRetryTemplate()
        template.registerListener(DefaultRetryListener())
        return template
    }

    /**
     * 创建指数退避重试模板
     */
    fun createExponentialBackoffRetryTemplate(
        maxAttempts: Int = 3,
        initialInterval: Long = 1000,
        multiplier: Double = 2.0,
        maxInterval: Long = 10000,
        retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
            Exception::class.java to true
        )
    ): RetryTemplate {
        val strategy = ExponentialBackoffRetryStrategy(
            maxAttempts,
            initialInterval,
            multiplier,
            maxInterval,
            retryableExceptions
        )
        val template = strategy.createRetryTemplate()
        template.registerListener(DefaultRetryListener())
        return template
    }

    /**
     * 创建SSE专用重试模板
     */
    fun createSSERetryTemplate(
        maxAttempts: Int = 5,
        initialInterval: Long = 1000,
        multiplier: Double = 2.0,
        maxInterval: Long = 30000,
        sseRetryListener: SSERetryListener
    ): RetryTemplate {
        val strategy = SSERetryStrategy(maxAttempts, initialInterval, multiplier, maxInterval)
        val template = strategy.createRetryTemplate()
        template.registerListener(sseRetryListener)
        return template
    }

    /**
     * 创建健康检查专用重试模板
     */
    fun createHealthCheckRetryTemplate(
        maxAttempts: Int = 3,
        initialInterval: Long = 2000,
        multiplier: Double = 2.0,
        maxInterval: Long = 10000,
        healthCheckRetryListener: HealthCheckRetryListener
    ): RetryTemplate {
        val strategy = HealthCheckRetryStrategy(maxAttempts, initialInterval, multiplier, maxInterval)
        val template = strategy.createRetryTemplate()
        template.registerListener(healthCheckRetryListener)
        return template
    }
} 