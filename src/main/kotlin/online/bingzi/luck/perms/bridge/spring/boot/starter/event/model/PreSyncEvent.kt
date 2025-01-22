package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 同步前事件
 * 用于处理同步前的准备工作
 */
class PreSyncEvent(
    source: EventSource,
    val sourceType: String,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.PRE_SYNC, priority) 