package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health

import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.RetryStrategy
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.math.pow

/**
 * 健康检查专用重试策略
 * 针对健康检查的特殊性进行优化
 *
 * @param maxAttempts 最大重试次数
 * @param initialInterval 初始重试间隔（毫秒）
 * @param multiplier 间隔时间乘数
 * @param maxInterval 最大重试间隔（毫秒）
 */
class HealthCheckRetryStrategy(
    private val maxAttempts: Int = 3,
    private val initialInterval: Long = 2000,
    private val multiplier: Double = 2.0,
    private val maxInterval: Long = 10000
) : RetryStrategy {

    // 健康检查相关的可重试异常
    private val retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
        IOException::class.java to true,
        ConnectException::class.java to true,
        SocketTimeoutException::class.java to true
    )

    override fun getMaxAttempts(): Int = maxAttempts

    override fun getBackoffPeriod(retryCount: Int): Long {
        val interval = initialInterval * multiplier.pow(retryCount.toDouble())
        return interval.toLong().coerceAtMost(maxInterval)
    }

    override fun shouldRetry(exception: Throwable): Boolean {
        return retryableExceptions[exception::class.java] ?: false
    }

    /**
     * 创建健康检查专用的RetryTemplate
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