package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent

/**
 * 日志广播事件
 * 用于处理系统日志广播
 */
class LogBroadcastEvent(
    source: Any,
    val message: String,
    val source: String,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.LOG_BROADCAST, priority) 