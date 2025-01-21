package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.AbstractLuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType

/**
 * 网络同步前事件
 * 在开始网络同步操作前触发
 */
data class PreNetworkSyncEvent(
    val syncId: String,
    val syncType: String,
    override val priority: EventPriority = EventPriority.HIGH
) : AbstractLuckPermsEvent(EventType.PRE_NETWORK_SYNC) 