package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 同步后事件
 * 用于处理同步完成后的操作
 */
class PostSyncEvent(
    source: EventSource,
    val sourceType: String,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.POST_SYNC, priority) 