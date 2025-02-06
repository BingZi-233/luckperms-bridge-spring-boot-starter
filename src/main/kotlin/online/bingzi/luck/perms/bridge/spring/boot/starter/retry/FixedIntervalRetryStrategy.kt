package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

/**
 * 固定间隔重试策略
 * 每次重试的间隔时间都是固定的
 *
 * @param maxAttempts 最大重试次数
 * @param interval 重试间隔（毫秒）
 */
class FixedIntervalRetryStrategy(
    private val maxAttempts: Int = 3,
    private val interval: Long = 1000
) : RetryStrategy {

    override fun getMaxAttempts(): Int = maxAttempts

    override fun getBackoffPeriod(retryCount: Int): Long = interval

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