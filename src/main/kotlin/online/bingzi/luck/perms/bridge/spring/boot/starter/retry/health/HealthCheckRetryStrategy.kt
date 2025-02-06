package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health

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
 * 健康检查专用重试策略
 * 该类实现了 RetryStrategy 接口，针对健康检查的特殊性进行优化
 * 提供重试机制，处理健康检查过程中可能出现的异常
 *
 * @param maxAttempts 最大重试次数，默认为3
 * @param initialInterval 初始重试间隔（毫秒），默认为1000毫秒
 * @param multiplier 间隔时间乘数，默认为2.0
 * @param maxInterval 最大重试间隔（毫秒），默认为30000毫秒
 */
class HealthCheckRetryStrategy(
    private val maxAttempts: Int = 3,
    private val initialInterval: Long = 1000,
    private val multiplier: Double = 2.0,
    private val maxInterval: Long = 30000
) : RetryStrategy {

    // 健康检查相关的可重试异常映射，异常类型与是否可重试的标志
    private val retryableExceptions: Map<Class<out Throwable>, Boolean> = mapOf(
        IOException::class.java to true,           // IO异常可重试
        SocketException::class.java to true,       // 套接字异常可重试
        SocketTimeoutException::class.java to true, // 套接字超时异常可重试
        SSLException::class.java to true,          // SSL异常可重试
        ConnectException::class.java to true       // 连接异常可重试
    )

    /**
     * 获取最大重试次数
     * 
     * @return 返回最大重试次数
     */
    override fun getMaxAttempts(): Int = maxAttempts

    /**
     * 计算退避期间
     * 根据当前重试次数计算下一次重试的时间间隔
     * 
     * @param retryCount 当前重试次数
     * @return 返回计算后的退避时间（毫秒）
     */
    override fun getBackoffPeriod(retryCount: Int): Long {
        // 计算当前重试次数对应的间隔时间
        val interval = initialInterval * multiplier.pow(retryCount.toDouble())
        // 返回不超过最大间隔的实际时间
        return interval.toLong().coerceAtMost(maxInterval)
    }

    /**
     * 检查是否应该重试
     * 
     * @param exception 异常对象，可以为null（表示正常关闭）
     * @return 如果应该重试返回true，否则返回false
     */
    override fun shouldRetry(exception: Throwable?): Boolean {
        // 如果异常为null，不进行重试
        if (exception == null) {
            return false
        }
        // 检查该异常是否在可重试的异常列表中
        return retryableExceptions[exception::class.java] ?: false
    }

    /**
     * 创建健康检查专用的 RetryTemplate
     * 该方法配置重试策略和退避策略，返回一个配置好的 RetryTemplate 对象
     * 
     * @return 返回配置好的 RetryTemplate
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