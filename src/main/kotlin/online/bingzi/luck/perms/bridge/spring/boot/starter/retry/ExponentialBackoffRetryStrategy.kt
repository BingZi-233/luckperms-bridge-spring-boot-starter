package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import kotlin.math.pow

/**
 * 指数退避重试策略类
 * 此类实现了一种重试机制，其中每次重试的间隔时间会按指数增长。
 * 主要用于在执行某些操作时，如果发生错误，可以通过重试的方式继续尝试，直到达到最大重试次数。
 *
 * @param maxAttempts 最大重试次数，默认为3次。
 * @param initialInterval 初始重试间隔（毫秒），默认为1000毫秒（1秒）。
 * @param multiplier 间隔时间的乘数，默认为2.0，表示每次重试间隔将是前一次的两倍。
 * @param maxInterval 最大重试间隔（毫秒），默认为30000毫秒（30秒），防止间隔时间过长。
 */
class ExponentialBackoffRetryStrategy(
    private val maxAttempts: Int = 3, // 最大重试次数
    private val initialInterval: Long = 1000, // 初始重试间隔时间（毫秒）
    private val multiplier: Double = 2.0, // 每次重试间隔的乘数
    private val maxInterval: Long = 30000 // 最大重试间隔时间（毫秒）
) : RetryStrategy { // 实现 RetryStrategy 接口

    /**
     * 获取最大重试次数
     * 
     * @return 最大重试次数
     */
    override fun getMaxAttempts(): Int = maxAttempts // 返回最大重试次数

    /**
     * 计算基于当前重试次数的退避时间
     * 
     * @param retryCount 当前的重试次数，类型为Int，表示已经重试的次数。
     * @return 返回计算出的退避时间（毫秒），类型为Long，表示下次重试前需要等待的时间。
     */
    override fun getBackoffPeriod(retryCount: Int): Long {
        // 计算当前重试次数的间隔时间
        val interval = initialInterval * multiplier.pow(retryCount.toDouble())
        // 返回计算后的间隔时间，确保不超过最大间隔时间
        return interval.toLong().coerceAtMost(maxInterval) 
    }

    /**
     * 检查是否应该进行重试
     * 
     * @param exception 异常对象，可以为null，表示操作正常完成。
     * @return 如果应该重试返回true，否则返回false。此实现中默认返回true，意味着只要没有达到最大重试次数就会继续重试。
     */
    override fun shouldRetry(exception: Throwable?): Boolean {
        // 默认情况下，只要没有达到最大重试次数就继续重试
        return true // 返回true表示可以继续重试
    }
} 