package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent

/**
 * 自定义消息事件
 * 用于处理自定义消息的接收
 */
class CustomMessageEvent(
    source: Any,
    val channel: String,
    val message: String,
    priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent(source, EventType.CUSTOM_MESSAGE, priority) 