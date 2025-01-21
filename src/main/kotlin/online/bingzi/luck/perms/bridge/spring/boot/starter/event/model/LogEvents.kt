package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.AbstractLuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType

/**
 * 日志广播事件
 * 用于处理系统日志广播
 */
data class LogBroadcastEvent(
    val message: String,
    val source: String,
    override val priority: EventPriority = EventPriority.NORMAL
) : AbstractLuckPermsEvent(EventType.LOG_BROADCAST) 