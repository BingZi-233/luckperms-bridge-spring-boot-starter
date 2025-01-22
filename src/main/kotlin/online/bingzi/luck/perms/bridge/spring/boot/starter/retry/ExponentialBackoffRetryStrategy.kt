package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import kotlin.math.pow

/**
 * 指数退避重试策略实现
 * @param maxAttempts 最大重试次数
 * @param initialInterval 初始重试间隔（毫秒）
 * @param multiplier 间隔时间乘数
 * @param maxInterval 最大重试间隔（毫秒）
 * @param retryableExceptions 可重试的异常类型
 */
class ExponentialBackoffRetryStrategy(
    private val maxAttempts: Int = 3,
    private val initialInterval: Long = 1000,
    private val multiplier: Double = 2.0,
    private val maxInterval: Long = 10000,
    private val retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
        Exception::class.java to true
    )
) : RetryStrategy {

    override fun getMaxAttempts(): Int = maxAttempts

    override fun getBackoffPeriod(retryCount: Int): Long {
        val interval = initialInterval * multiplier.pow(retryCount.toDouble())
        return interval.toLong().coerceAtMost(maxInterval)
    }

    override fun shouldRetry(exception: Throwable): Boolean {
        return retryableExceptions[exception::class.java] ?: false
    }

    /**
     * 创建Spring Retry的RetryTemplate
     */
    fun createRetryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()
        
        // 设置重试策略
        val retryPolicy: RetryPolicy = SimpleRetryPolicy(maxAttempts, retryableExceptions)
        retryTemplate.setRetryPolicy(retryPolicy)
        
        // 设置退避策略
        val backOffPolicy = ExponentialBackOffPolicy()
        backOffPolicy.initialInterval = initialInterval
        backOffPolicy.multiplier = multiplier
        backOffPolicy.maxInterval = maxInterval
        retryTemplate.setBackOffPolicy(backOffPolicy)
        
        return retryTemplate
    }
} 