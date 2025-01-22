package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 网络同步前事件
 * 用于处理网络同步前的准备工作
 */
class PreNetworkSyncEvent(
    source: EventSource,
    val sourceType: String,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.PRE_NETWORK_SYNC, priority)