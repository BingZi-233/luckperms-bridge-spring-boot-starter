package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 网络同步后事件
 * 用于处理网络同步完成后的操作
 */
class PostNetworkSyncEvent(
    source: EventSource,
    val sourceType: String,
    val success: Boolean,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.POST_NETWORK_SYNC, priority) 