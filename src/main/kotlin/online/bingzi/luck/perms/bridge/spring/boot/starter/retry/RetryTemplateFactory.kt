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
 * 该对象用于创建和管理不同类型的重试模板，包括固定间隔重试、指数退避重试、SSE专用重试和健康检查专用重试。
 */
object RetryTemplateFactory {
    /**
     * 创建固定间隔重试模板
     * 
     * @param maxAttempts 最大重试次数，默认为3次
     * @param interval 固定的重试间隔时间，单位为毫秒，默认为1000毫秒
     * @return 返回配置好的RetryTemplate实例
     */
    fun createFixedIntervalRetryTemplate(
        maxAttempts: Int = 3,
        interval: Long = 1000
    ): RetryTemplate {
        // 创建固定间隔重试策略
        val strategy = FixedIntervalRetryStrategy(maxAttempts, interval)
        val template = RetryTemplate()
        
        // 设置重试策略为简单重试策略
        val retryPolicy = SimpleRetryPolicy(strategy.getMaxAttempts())
        template.setRetryPolicy(retryPolicy)
        
        // 设置退避策略为固定间隔退避策略
        val backOffPolicy = FixedBackOffPolicy()
        backOffPolicy.backOffPeriod = strategy.getBackoffPeriod(0)
        template.setBackOffPolicy(backOffPolicy)
        
        // 注册默认重试监听器
        template.registerListener(DefaultRetryListener())
        return template
    }

    /**
     * 创建指数退避重试模板
     * 
     * @param maxAttempts 最大重试次数，默认为3次
     * @param initialInterval 初始重试间隔时间，单位为毫秒，默认为1000毫秒
     * @param multiplier 指数退避的乘数，默认为2.0
     * @param maxInterval 最大重试间隔时间，单位为毫秒，默认为10000毫秒
     * @return 返回配置好的RetryTemplate实例
     */
    fun createExponentialBackoffRetryTemplate(
        maxAttempts: Int = 3,
        initialInterval: Long = 1000,
        multiplier: Double = 2.0,
        maxInterval: Long = 10000
    ): RetryTemplate {
        // 创建指数退避重试策略
        val strategy = ExponentialBackoffRetryStrategy(
            maxAttempts,
            initialInterval,
            multiplier,
            maxInterval
        )
        val template = RetryTemplate()
        
        // 设置重试策略为简单重试策略
        val retryPolicy = SimpleRetryPolicy(strategy.getMaxAttempts())
        template.setRetryPolicy(retryPolicy)
        
        // 设置退避策略为指数退避策略
        val backOffPolicy = ExponentialBackOffPolicy()
        backOffPolicy.initialInterval = initialInterval
        backOffPolicy.multiplier = multiplier
        backOffPolicy.maxInterval = maxInterval
        template.setBackOffPolicy(backOffPolicy)
        
        // 注册默认重试监听器
        template.registerListener(DefaultRetryListener())
        return template
    }

    /**
     * 创建SSE专用重试模板
     * 
     * @param maxAttempts 最大重试次数，默认为5次
     * @param initialInterval 初始重试间隔时间，单位为毫秒，默认为1000毫秒
     * @param multiplier 指数退避的乘数，默认为2.0
     * @param maxInterval 最大重试间隔时间，单位为毫秒，默认为30000毫秒
     * @param sseRetryListener SSE重试监听器的实例，用于监听重试事件
     * @return 返回配置好的RetryTemplate实例
     */
    fun createSSERetryTemplate(
        maxAttempts: Int = 5,
        initialInterval: Long = 1000,
        multiplier: Double = 2.0,
        maxInterval: Long = 30000,
        sseRetryListener: SSERetryListener
    ): RetryTemplate {
        // 创建SSE重试策略
        val strategy = SSERetryStrategy(maxAttempts, initialInterval, multiplier, maxInterval)
        val template = strategy.createRetryTemplate()
        
        // 注册SSE重试监听器
        template.registerListener(sseRetryListener)
        return template
    }

    /**
     * 创建健康检查专用重试模板
     * 
     * @param maxAttempts 最大重试次数，默认为3次
     * @param initialInterval 初始重试间隔时间，单位为毫秒，默认为2000毫秒
     * @param multiplier 指数退避的乘数，默认为2.0
     * @param maxInterval 最大重试间隔时间，单位为毫秒，默认为10000毫秒
     * @param healthCheckRetryListener 健康检查重试监听器的实例，用于监听重试事件
     * @return 返回配置好的RetryTemplate实例
     */
    fun createHealthCheckRetryTemplate(
        maxAttempts: Int = 3,
        initialInterval: Long = 2000,
        multiplier: Double = 2.0,
        maxInterval: Long = 10000,
        healthCheckRetryListener: HealthCheckRetryListener
    ): RetryTemplate {
        // 创建健康检查重试策略
        val strategy = HealthCheckRetryStrategy(maxAttempts, initialInterval, multiplier, maxInterval)
        val template = strategy.createRetryTemplate()
        
        // 注册健康检查重试监听器
        template.registerListener(healthCheckRetryListener)
        return template
    }
}