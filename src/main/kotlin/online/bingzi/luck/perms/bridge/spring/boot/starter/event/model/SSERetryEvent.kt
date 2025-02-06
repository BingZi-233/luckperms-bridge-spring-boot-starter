package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import org.springframework.context.ApplicationEvent

/**
 * SSE重试事件类
 *
 * 该类用于表示在SSE（Server-Sent Events）连接中发生的重试事件。
 * 它扩展自Spring的ApplicationEvent类，使得该事件可以在Spring的事件发布和监听机制中使用。
 * 主要功能是携带SSE连接的相关信息，包括端点URL和重试次数。
 *
 * @property endpoint SSE端点URL，表示与服务器之间的连接地址。
 * @property retryCount 重试次数，表示在连接失败后进行的重试次数。
 */
class SSERetryEvent(
    source: EventSource,  // 事件源，表示触发此事件的EventSource对象
    val endpoint: String,  // SSE连接的端点URL
    val retryCount: Int  // 连接重试的次数
) : ApplicationEvent(source)  // 继承自ApplicationEvent，表示一个应用事件