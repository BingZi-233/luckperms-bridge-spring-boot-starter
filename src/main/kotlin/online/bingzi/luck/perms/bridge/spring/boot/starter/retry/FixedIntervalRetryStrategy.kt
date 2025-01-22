package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import org.springframework.retry.RetryPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

/**
 * 固定间隔重试策略实现
 * @param maxAttempts 最大重试次数
 * @param backoffPeriod 重试间隔时间（毫秒）
 * @param retryableExceptions 可重试的异常类型
 */
class FixedIntervalRetryStrategy(
    private val maxAttempts: Int = 3,
    private val backoffPeriod: Long = 1000,
    private val retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
        Exception::class.java to true
    )
) : RetryStrategy {

    override fun getMaxAttempts(): Int = maxAttempts

    override fun getBackoffPeriod(retryCount: Int): Long = backoffPeriod

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
        
        return retryTemplate
    }
} 