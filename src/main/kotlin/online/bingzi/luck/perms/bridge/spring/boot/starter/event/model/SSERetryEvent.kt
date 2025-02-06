package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import org.springframework.context.ApplicationEvent

/**
 * SSE重试事件
 *
 * @property endpoint SSE端点URL
 * @property retryCount 重试次数
 */
class SSERetryEvent(
    source: EventSource,
    val endpoint: String,
    val retryCount: Int
) : ApplicationEvent(source) 