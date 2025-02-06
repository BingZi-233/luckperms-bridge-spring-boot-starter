package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * SSE连接状态事件
 *
 * 此类用于跟踪和通知SSE（Server-Sent Events）连接的状态变化。
 * 在连接建立、关闭或发生错误时会触发此事件，允许系统监控和响应SSE连接的生命周期变化。
 * 该事件包含当前连接状态、目标端点URL、状态描述信息和错误信息（如果有）。
 *
 * @property state 当前的连接状态，类型为ConnectionStateType，表示连接的当前状态。
 * @property endpoint 连接的目标端点URL，类型为String，表示要连接的服务器地址。
 * @property message 可选的状态描述信息，类型为String?，可以提供连接状态的附加信息。
 * @property error 可选的错误信息，类型为Throwable?，当连接失败时提供相关异常信息。
 */
class ConnectionStateEvent(
    source: EventSource, // 事件源，表示产生该事件的源对象
    val state: ConnectionStateType, // 当前连接状态
    val endpoint: String, // 连接的目标端点URL
    val message: String? = null, // 可选的状态描述信息
    val error: Throwable? = null, // 可选的错误信息，仅在连接失败时存在
    priority: EventPriority = EventPriority.HIGH // 事件优先级，默认值为高优先级
) : LuckPermsEvent(source, EventType.CONNECTION_STATE, priority) // 继承自LuckPermsEvent，表示这是一个特定类型的事件