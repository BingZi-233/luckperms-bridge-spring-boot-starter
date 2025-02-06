package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 日志广播事件类
 * 该类用于表示一个日志广播事件，包含事件的源、消息内容、源类型及事件优先级等信息。
 * 继承自LuckPermsEvent，表示这是一个特定类型的事件。
 *
 * @property message 事件中包含的日志消息内容。
 * @property sourceType 表示日志消息的来源类型，例如系统、用户等。
 * @property priority 事件的优先级，默认为普通优先级（EventPriority.NORMAL）。
 */
class LogBroadcastEvent(
    source: EventSource, // 事件源，表示该事件的来源，可以是一个服务器发送的事件流
    val message: String, // 日志消息的内容
    val sourceType: String, // 消息来源的类型
    priority: EventPriority = EventPriority.NORMAL // 事件的优先级，默认为普通优先级
) : LuckPermsEvent(source, EventType.LOG_BROADCAST, priority) // 调用父类构造函数，初始化事件类型为日志广播事件