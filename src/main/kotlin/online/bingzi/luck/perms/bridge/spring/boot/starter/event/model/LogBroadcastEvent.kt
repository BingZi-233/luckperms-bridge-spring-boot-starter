package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 日志广播事件
 * 用于处理系统日志广播
 */
class LogBroadcastEvent(
    source: EventSource,
    val message: String,
    val sourceType: String,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.LOG_BROADCAST, priority) 