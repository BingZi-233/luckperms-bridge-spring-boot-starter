package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

/**
 * 重试策略接口
 * 此接口定义了重试操作的基本行为，包括获取最大重试次数、获取重试间隔时间以及判断是否应该重试。
 * 该接口可以被实现为不同的重试策略，以满足不同的业务需求。
 */
interface RetryStrategy {
    /**
     * 获取最大重试次数
     * @return 返回最大重试次数的整数值
     */
    fun getMaxAttempts(): Int

    /**
     * 获取重试间隔时间（毫秒）
     * 根据当前重试次数返回重试的间隔时间。
     * @param retryCount 当前重试次数，类型为Int，表示已经重试的次数。
     * @return 返回重试间隔时间，单位为毫秒（Long类型）。
     */
    fun getBackoffPeriod(retryCount: Int): Long

    /**
     * 判断是否应该重试
     * 根据传入的异常情况判断是否需要进行重试。
     * @param exception 导致失败的异常，可以为null（表示正常关闭），类型为Throwable。
     * @return 如果应该重试返回true，否则返回false。
     */
    fun shouldRetry(exception: Throwable?): Boolean
}