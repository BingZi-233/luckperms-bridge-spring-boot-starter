package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent

/**
 * 网络同步前事件
 * 在开始网络同步操作前触发
 */
class PreNetworkSyncEvent(
    source: Any,
    val syncId: String,
    val syncType: String,
    priority: EventPriority = EventPriority.HIGH
) : LuckPermsEvent(source, EventType.PRE_NETWORK_SYNC, priority)

/**
 * 网络同步后事件
 * 在网络同步操作完成后触发
 */
class PostNetworkSyncEvent(
    source: Any,
    val syncId: String,
    val syncType: String,
    val didSyncOccur: Boolean,
    priority: EventPriority = EventPriority.HIGH
) : LuckPermsEvent(source, EventType.POST_NETWORK_SYNC, priority) 