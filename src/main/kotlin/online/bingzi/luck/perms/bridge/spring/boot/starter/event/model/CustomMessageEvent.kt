package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * 自定义消息事件
 * 用于处理自定义消息
 */
class CustomMessageEvent(
    source: EventSource,
    val message: String,
    val channel: String,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.CUSTOM_MESSAGE, priority) 