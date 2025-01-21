package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.AbstractLuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.EventType

/**
 * 自定义消息事件
 * 用于处理自定义消息的接收
 */
data class CustomMessageEvent(
    val channel: String,
    val message: String,
    override val priority: EventPriority = EventPriority.NORMAL
) : AbstractLuckPermsEvent(EventType.CUSTOM_MESSAGE) 