package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 同步后事件类
 * 该类用于表示在同步操作完成后触发的事件。
 * 主要用于处理与同步相关的后续操作。
 * 此类继承自LuckPermsEvent，包含事件源、事件类型及优先级等信息。
 */
class PostSyncEvent(
    source: EventSource, // 事件源，表示事件的来源
    val sourceType: String, // 源类型，表示事件源的类型
    priority: EventPriority = EventPriority.NORMAL // 事件优先级，默认为普通优先级
) : LuckPermsEvent(source, EventType.POST_SYNC, priority) // 调用父类构造函数，设置事件源、事件类型以及优先级