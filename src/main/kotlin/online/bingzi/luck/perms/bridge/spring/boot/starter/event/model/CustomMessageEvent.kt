package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 自定义消息事件类
 * 该类用于表示一个自定义的消息事件，继承自LuckPermsEvent。
 * 它包含了消息内容、频道信息以及事件的优先级。
 * 主要用于处理来自特定源的消息事件。
 *
 * @property message 消息内容，类型为String，表示事件中传递的具体消息。
 * @property channel 频道信息，类型为String，表示事件发生的频道或来源。
 * @property priority 事件优先级，类型为EventPriority，默认为NORMAL，表示事件处理的优先级。
 */
class CustomMessageEvent(
    source: EventSource, // 事件源，类型为EventSource，表示触发事件的源。
    val message: String, // 消息内容，类型为String，表示事件传递的消息。
    val channel: String, // 频道信息，类型为String，表示事件发生的频道。
    priority: EventPriority = EventPriority.NORMAL // 事件优先级，类型为EventPriority，默认为NORMAL。
) : LuckPermsEvent(source, EventType.CUSTOM_MESSAGE, priority) // 调用父类构造函数，设置事件源、事件类型和优先级。