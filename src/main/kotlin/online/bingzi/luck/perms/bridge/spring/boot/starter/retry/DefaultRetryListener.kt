package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener

/**
 * 默认重试监听器
 * 该类实现了 RetryListener 接口，用于监控和记录重试操作的过程中的关键事件。
 * 它在重试操作的不同阶段（打开、关闭、错误）提供日志记录功能，以便于调试和分析。
 */
class DefaultRetryListener : RetryListener {
    // 创建日志记录器，用于输出重试过程中的日志
    private val log = LoggerFactory.getLogger(DefaultRetryListener::class.java)

    /**
     * 当重试操作发生错误时调用
     * @param context 当前的重试上下文，包含重试的状态信息
     * @param callback 执行重试的回调函数
     * @param throwable 发生的异常，表示重试失败的原因
     */
    override fun <T, E : Throwable> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        // 记录重试操作失败的相关信息，包括当前重试次数和异常信息
        log.warn(
            "重试操作失败 - 当前重试次数: ${context.retryCount}, " +
            "异常类型: ${throwable.javaClass.simpleName}, " +
            "异常信息: ${throwable.message}"
        )
    }

    /**
     * 当重试操作关闭时调用
     * @param context 当前的重试上下文，包含重试的状态信息
     * @param callback 执行重试的回调函数
     * @param throwable 如果重试最终失败，则包含最后抛出的异常；如果成功，则为 null
     */
    override fun <T, E : Throwable> close(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable?
    ) {
        // 如果 throwable 不为 null，表示重试最终失败，记录错误日志；否则记录成功日志
        if (throwable != null) {
            log.error(
                "重试操作最终失败 - 总重试次数: ${context.retryCount}, " +
                "异常类型: ${throwable.javaClass.simpleName}, " +
                "异常信息: ${throwable.message}"
            )
        } else {
            log.info("重试操作成功完成 - 总重试次数: ${context.retryCount}")
        }
    }

    /**
     * 在重试操作开始时调用
     * @param context 当前的重试上下文，包含重试的状态信息
     * @param callback 执行重试的回调函数
     * @return 返回 true 表示允许重试，返回 false 表示不允许重试
     */
    override fun <T, E : Throwable> open(
        context: RetryContext,
        callback: RetryCallback<T, E>
    ): Boolean {
        // 如果当前重试次数大于 0，记录当前重试次数
        if (context.retryCount > 0) {
            log.info("开始第 ${context.retryCount} 次重试")
        }
        return true // 返回 true 表示允许重试
    }
}