package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse

import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.RetryStrategy
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException
import kotlin.math.pow

/**
 * SSE专用重试策略
 * 针对SSE连接的特殊性进行优化
 *
 * @param maxAttempts 最大重试次数
 * @param initialInterval 初始重试间隔（毫秒）
 * @param multiplier 间隔时间乘数
 * @param maxInterval 最大重试间隔（毫秒）
 */
class SSERetryStrategy(
    private val maxAttempts: Int = 5,
    private val initialInterval: Long = 1000,
    private val multiplier: Double = 2.0,
    private val maxInterval: Long = 30000
) : RetryStrategy {

    // SSE连接相关的可重试异常
    private val retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
        IOException::class.java to true,
        SocketException::class.java to true,
        SocketTimeoutException::class.java to true,
        SSLException::class.java to true,
        ConnectException::class.java to true
    )

    override fun getMaxAttempts(): Int = maxAttempts

    override fun getBackoffPeriod(retryCount: Int): Long {
        val interval = initialInterval * multiplier.pow(retryCount.toDouble())
        return interval.toLong().coerceAtMost(maxInterval)
    }

    /**
     * 检查是否应该重试
     * 
     * @param exception 异常对象，可以为null（表示正常关闭）
     * @return 如果应该重试返回true，否则返回false
     */
    override fun shouldRetry(exception: Throwable?): Boolean {
        // 如果异常为null（正常关闭），也应该重试
        if (exception == null) {
            return true
        }
        // 检查是否是可重试的异常
        return retryableExceptions[exception::class.java] ?: false
    }

    /**
     * 创建SSE专用的RetryTemplate
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