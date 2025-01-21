package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.AbstractLuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType

/**
 * 同步后事件
 * 在同步操作完成后触发
 */
data class PostSyncEvent(
    val cause: String,
    val didSyncOccur: Boolean,
    override val priority: EventPriority = EventPriority.HIGH
) : AbstractLuckPermsEvent(EventType.POST_SYNC) 