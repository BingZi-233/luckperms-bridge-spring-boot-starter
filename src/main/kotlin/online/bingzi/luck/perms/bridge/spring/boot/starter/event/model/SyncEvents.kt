package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent

/**
 * 同步前事件
 * 在开始同步操作前触发
 */
class PreSyncEvent(
    source: EventSource,
    val cause: String,
    priority: EventPriority = EventPriority.HIGH
) : LuckPermsEvent(source, EventType.PRE_SYNC, priority)

/**
 * 同步后事件
 * 在同步操作完成后触发
 */
class PostSyncEvent(
    source: EventSource,
    val cause: String,
    val didSyncOccur: Boolean,
    priority: EventPriority = EventPriority.HIGH
) : LuckPermsEvent(source, EventType.POST_SYNC, priority) 