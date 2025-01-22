package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

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
} 