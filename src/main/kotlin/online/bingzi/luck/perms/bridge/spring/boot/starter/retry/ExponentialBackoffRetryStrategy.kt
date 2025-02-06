package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import kotlin.math.pow

/**
 * 指数退避重试策略
 * 每次重试的间隔时间会按指数增长
 *
 * @param maxAttempts 最大重试次数
 * @param initialInterval 初始重试间隔（毫秒）
 * @param multiplier 间隔时间乘数
 * @param maxInterval 最大重试间隔（毫秒）
 */
class ExponentialBackoffRetryStrategy(
    private val maxAttempts: Int = 3,
    private val initialInterval: Long = 1000,
    private val multiplier: Double = 2.0,
    private val maxInterval: Long = 30000
) : RetryStrategy {

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
        // 默认情况下，只要没有达到最大重试次数就继续重试
        return true
    }
} 