package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.state.ConnectionState
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * SSE连接状态事件
 *
 * 用于跟踪和通知SSE连接的状态变化。此事件在连接建立、关闭或发生错误时触发。
 * 通过此事件，系统可以监控和响应SSE连接的生命周期变化。
 *
 * @property state 当前的连接状态
 * @property endpoint 连接的目标端点URL
 * @property message 可选的状态描述信息
 * @property error 可选的错误信息，仅在连接失败时存在
 */
class ConnectionStateEvent(
    source: EventSource,
    val state: ConnectionState,
    val endpoint: String,
    val message: String? = null,
    val error: Throwable? = null,
    priority: EventPriority = EventPriority.HIGH
) : LuckPermsEvent(source, EventType.CONNECTION_STATE, priority)