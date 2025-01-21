package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.AbstractLuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType

/**
 * 网络同步后事件
 * 在网络同步操作完成后触发
 */
data class PostNetworkSyncEvent(
    val syncId: String,
    val syncType: String,
    val didSyncOccur: Boolean,
    override val priority: EventPriority = EventPriority.HIGH
) : AbstractLuckPermsEvent(EventType.POST_NETWORK_SYNC) 