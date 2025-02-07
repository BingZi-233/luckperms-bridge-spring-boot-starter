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
 * @param maxAttempts 最大重试次数，默认为6次
 * @param initialInterval 初始重试间隔（单位：毫秒），默认为1500毫秒
 * @param multiplier 间隔时间的乘数，默认为1.8
 * @param maxInterval 最大重试间隔（单位：毫秒），默认为45000毫秒
 */
class SSERetryStrategy(
    private val maxAttempts: Int = 6,
    private val initialInterval: Long = 1500,
    private val multiplier: Double = 1.8,
    private val maxInterval: Long = 45000
) : RetryStrategy {

    // 定义SSE连接相关的可重试异常
    private val retryableExceptions: Map<Class<out Throwable>, Boolean> = mutableMapOf<Class<out Throwable>, Boolean>().also {
        it[IOException::class.java] = true           // IO异常（网络、文件等）
        it[SocketException::class.java] = true       // 套接字异常
        it[SocketTimeoutException::class.java] = true // 套接字超时
        it[SSLException::class.java] = true          // SSL/TLS异常
        it[ConnectException::class.java] = true      // 连接异常
    }

    /**
     * 获取最大重试次数
     * 
     * @return 返回最大重试次数
     */
    override fun getMaxAttempts(): Int = maxAttempts

    /**
     * 计算基于重试次数的退避时间
     * 使用指数退避算法，但增加了随机因子以避免多个客户端同时重试
     * 
     * @param retryCount 当前的重试次数
     * @return 返回计算得到的退避时间（单位：毫秒）
     */
    override fun getBackoffPeriod(retryCount: Int): Long {
        // 计算基础间隔
        val baseInterval = initialInterval * multiplier.pow(retryCount.toDouble())
        // 添加随机因子 (±20%)
        val randomFactor = 0.8 + Math.random() * 0.4
        // 计算最终间隔时间
        val interval = (baseInterval * randomFactor).toLong()
        // 限制最大间隔时间
        return interval.coerceAtMost(maxInterval)
    }

    /**
     * 检查是否应该进行重试
     * 
     * @param exception 异常对象，如果为null表示正常关闭
     * @return 如果应该重试返回true，否则返回false
     */
    override fun shouldRetry(exception: Throwable?): Boolean {
        // 如果异常为null（表示正常关闭），不进行重试
        if (exception == null) {
            return false
        }
        
        // 检查异常类型是否可重试
        val shouldRetry = retryableExceptions[exception::class.java] ?: false
        // 如果当前异常不可重试，检查cause
        if (!shouldRetry && exception.cause != null) {
            val cause = exception.cause
            if (cause != null) {
                return retryableExceptions[cause.javaClass] ?: false
            }
        }
        return shouldRetry
    }

    /**
     * 创建SSE专用的RetryTemplate
     * 
     * @return 返回配置好的RetryTemplate实例
     */
    fun createRetryTemplate(): RetryTemplate {
        // 创建一个新的RetryTemplate实例
        val retryTemplate = RetryTemplate()
        
        // 设置重试策略，包含可重试的异常
        val retryPolicy: RetryPolicy = SimpleRetryPolicy(maxAttempts, retryableExceptions)
        retryTemplate.setRetryPolicy(retryPolicy)
        
        // 设置退避策略为指数退避
        val backOffPolicy = ExponentialBackOffPolicy().apply {
            initialInterval = this@SSERetryStrategy.initialInterval
            multiplier = this@SSERetryStrategy.multiplier
            maxInterval = this@SSERetryStrategy.maxInterval
        }
        retryTemplate.setBackOffPolicy(backOffPolicy)
        
        return retryTemplate
    }
}