package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent

/**
 * 网络同步前事件
 * 在开始网络同步操作前触发
 */
class PreNetworkSyncEvent(
    source: EventSource,
    val syncId: String,
    val syncType: String,
    priority: EventPriority = EventPriority.HIGH
) : LuckPermsEvent(source, EventType.PRE_NETWORK_SYNC, priority)