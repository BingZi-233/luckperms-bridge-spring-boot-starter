package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 网络同步后事件
 * 该类用于表示网络同步完成后的事件，包含了网络同步的状态和优先级等信息。
 * 主要用于在网络同步成功或失败后触发相应的事件处理逻辑。
 */
class PostNetworkSyncEvent(
    // 事件源，表示事件的来源
    source: EventSource,
    
    // 源类型，描述事件源的类型，例如 "API" 或 "DATABASE"
    val sourceType: String,
    
    // 表示网络同步是否成功，成功为 true，失败为 false
    val success: Boolean,
    
    // 事件的优先级，默认为 NORMAL，表示普通优先级
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.POST_NETWORK_SYNC, priority) 
