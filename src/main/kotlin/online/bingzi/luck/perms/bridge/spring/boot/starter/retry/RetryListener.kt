package online.bingzi.luck.perms.bridge.spring.boot.starter.retry

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.listener.RetryListenerSupport

/**
 * 默认重试监听器实现
 * 用于记录重试过程中的关键事件
 */
class DefaultRetryListener : RetryListenerSupport() {
    private val log = LoggerFactory.getLogger(DefaultRetryListener::class.java)

    override fun <T, E : Throwable> onError(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable
    ) {
        log.warn(
            "重试操作失败 - 当前重试次数: ${context.retryCount}, " +
            "异常类型: ${throwable.javaClass.simpleName}, " +
            "异常信息: ${throwable.message}"
        )
        super.onError(context, callback, throwable)
    }

    override fun <T, E : Throwable> close(
        context: RetryContext,
        callback: RetryCallback<T, E>,
        throwable: Throwable?
    ) {
        if (throwable != null) {
            log.error(
                "重试操作最终失败 - 总重试次数: ${context.retryCount}, " +
                "异常类型: ${throwable.javaClass.simpleName}, " +
                "异常信息: ${throwable.message}"
            )
        } else {
            log.info("重试操作成功完成 - 总重试次数: ${context.retryCount}")
        }
        super.close(context, callback, throwable)
    }
} 