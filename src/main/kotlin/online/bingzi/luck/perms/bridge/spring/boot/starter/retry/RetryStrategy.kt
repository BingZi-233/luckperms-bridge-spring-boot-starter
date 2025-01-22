package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

/**
 * 重试策略接口
 * 定义了重试操作的基本行为
 */
interface RetryStrategy {
    /**
     * 获取最大重试次数
     */
    fun getMaxAttempts(): Int

    /**
     * 获取重试间隔时间（毫秒）
     * @param retryCount 当前重试次数
     */
    fun getBackoffPeriod(retryCount: Int): Long

    /**
     * 判断是否应该重试
     * @param exception 导致失败的异常
     */
    fun shouldRetry(exception: Throwable): Boolean
} 