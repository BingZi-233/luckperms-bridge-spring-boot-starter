package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.AbstractLuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType

/**
 * 同步前事件
 * 在开始同步操作前触发
 */
data class PreSyncEvent(
    val cause: String,
    override val priority: EventPriority = EventPriority.HIGH
) : AbstractLuckPermsEvent(EventType.PRE_SYNC) 