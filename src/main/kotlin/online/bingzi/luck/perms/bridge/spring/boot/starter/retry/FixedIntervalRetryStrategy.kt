package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

/**
 * 固定间隔重试策略类
 * 该类实现了RetryStrategy接口，提供了一种固定时间间隔的重试机制。
 * 每次重试之间的时间间隔是固定的，适用于需要在固定时间后进行重试的场景。
 * 
 * @property maxAttempts 最大重试次数，默认值为3。
 * @property interval 重试间隔，单位为毫秒，默认值为1000毫秒（即1秒）。
 */
class FixedIntervalRetryStrategy(
    private val maxAttempts: Int = 3, // 最大重试次数
    private val interval: Long = 1000 // 重试间隔（毫秒）
) : RetryStrategy {

    /**
     * 获取最大重试次数
     * 
     * @return 返回最大重试次数的整数值
     */
    override fun getMaxAttempts(): Int = maxAttempts

    /**
     * 获取重试的间隔时间
     * 
     * @param retryCount 当前的重试次数，表示已经进行了多少次重试
     * @return 返回固定的重试间隔时间（毫秒）
     */
    override fun getBackoffPeriod(retryCount: Int): Long = interval

    /**
     * 检查是否应该进行重试
     * 
     * @param exception 异常对象，可以为null，表示正常关闭情况
     * @return 如果应该重试返回true，否则返回false
     * 
     * 说明：在默认情况下，只要没有达到最大重试次数就会继续重试。
     */
    override fun shouldRetry(exception: Throwable?): Boolean {
        // 默认情况下，只要没有达到最大重试次数就继续重试
        return true
    }
}