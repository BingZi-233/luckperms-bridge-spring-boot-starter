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
 * 该类实现了RetryStrategy接口，提供针对SSE（Server-Sent Events）连接的重试机制。
 * 主要通过配置最大重试次数、初始间隔、乘数和最大间隔来控制重试行为。
 *
 * @param maxAttempts 最大重试次数，默认为5次
 * @param initialInterval 初始重试间隔（单位：毫秒），默认为1000毫秒
 * @param multiplier 间隔时间的乘数，默认为2.0
 * @param maxInterval 最大重试间隔（单位：毫秒），默认为30000毫秒
 */
class SSERetryStrategy(
    private val maxAttempts: Int = 5,
    private val initialInterval: Long = 1000,
    private val multiplier: Double = 2.0,
    private val maxInterval: Long = 30000
) : RetryStrategy {

    // 定义SSE连接相关的可重试异常
    // 这些异常将会触发重试机制
    private val retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
        IOException::class.java to true,
        SocketException::class.java to true,
        SocketTimeoutException::class.java to true,
        SSLException::class.java to true,
        ConnectException::class.java to true
    )

    /**
     * 获取最大重试次数
     * 
     * @return 返回最大重试次数
     */
    override fun getMaxAttempts(): Int = maxAttempts

    /**
     * 计算基于重试次数的退避时间
     * 
     * @param retryCount 当前的重试次数
     * @return 返回计算得到的退避时间（单位：毫秒）
     */
    override fun getBackoffPeriod(retryCount: Int): Long {
        // 计算下一个重试间隔，使用指数退避算法
        val interval = initialInterval * multiplier.pow(retryCount.toDouble())
        // 限制最大间隔时间
        return interval.toLong().coerceAtMost(maxInterval)
    }

    /**
     * 检查是否应该进行重试
     * 
     * @param exception 异常对象，如果为null表示正常关闭
     * @return 如果应该重试返回true，否则返回false
     */
    override fun shouldRetry(exception: Throwable?): Boolean {
        // 如果异常为null（表示正常关闭），也应该进行重试
        if (exception == null) {
            return true
        }
        // 检查是否是可重试的异常，如果是则返回true
        return retryableExceptions[exception::class.java] ?: false
    }

    /**
     * 创建SSE专用的RetryTemplate
     * 
     * @return 返回配置好的RetryTemplate实例
     */
    fun createRetryTemplate(): RetryTemplate {
        // 创建一个新的RetryTemplate实例
        val retryTemplate = RetryTemplate()
        
        // 设置重试策略
        val retryPolicy: RetryPolicy = SimpleRetryPolicy(maxAttempts, retryableExceptions)
        retryTemplate.setRetryPolicy(retryPolicy)
        
        // 设置退避策略为指数退避
        val backOffPolicy = ExponentialBackOffPolicy()
        backOffPolicy.initialInterval = initialInterval // 设置初始间隔
        backOffPolicy.multiplier = multiplier // 设置乘数
        backOffPolicy.maxInterval = maxInterval // 设置最大间隔
        retryTemplate.setBackOffPolicy(backOffPolicy)
        
        // 返回配置完成的RetryTemplate
        return retryTemplate
    }
}