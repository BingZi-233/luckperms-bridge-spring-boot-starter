package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 网络同步前事件类
 * 该类表示在进行网络同步之前的事件，用于处理与网络同步相关的准备工作。
 * 继承自LuckPermsEvent，包含事件源、事件类型和事件优先级等信息。
 */
class PreNetworkSyncEvent(
    source: EventSource, // 事件源，表示触发该事件的来源对象
    val sourceType: String, // 源类型，表示事件源的类型（例如：客户端、服务端等）
    priority: EventPriority = EventPriority.NORMAL // 事件优先级，默认为普通优先级
) : LuckPermsEvent(source, EventType.PRE_NETWORK_SYNC, priority) // 调用父类构造函数，设置事件源、事件类型和优先级