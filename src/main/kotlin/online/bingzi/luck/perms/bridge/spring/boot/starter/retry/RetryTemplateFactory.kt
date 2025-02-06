package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckRetryListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckRetryStrategy
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryStrategy
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
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
        interval: Long = 1000
    ): RetryTemplate {
        val strategy = FixedIntervalRetryStrategy(maxAttempts, interval)
        val template = RetryTemplate()
        
        // 设置重试策略
        val retryPolicy = SimpleRetryPolicy(strategy.getMaxAttempts())
        template.setRetryPolicy(retryPolicy)
        
        // 设置退避策略
        val backOffPolicy = FixedBackOffPolicy()
        backOffPolicy.backOffPeriod = strategy.getBackoffPeriod(0)
        template.setBackOffPolicy(backOffPolicy)
        
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
        maxInterval: Long = 10000
    ): RetryTemplate {
        val strategy = ExponentialBackoffRetryStrategy(
            maxAttempts,
            initialInterval,
            multiplier,
            maxInterval
        )
        val template = RetryTemplate()
        
        // 设置重试策略
        val retryPolicy = SimpleRetryPolicy(strategy.getMaxAttempts())
        template.setRetryPolicy(retryPolicy)
        
        // 设置退避策略
        val backOffPolicy = ExponentialBackOffPolicy()
        backOffPolicy.initialInterval = initialInterval
        backOffPolicy.multiplier = multiplier
        backOffPolicy.maxInterval = maxInterval
        template.setBackOffPolicy(backOffPolicy)
        
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