package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import java.time.Instant

/**
 * LuckPerms事件基础接口
 * 所有LuckPerms事件都必须实现此接口
 */
interface LuckPermsEvent {
    /**
     * 事件唯一标识符
     */
    val eventId: String

    /**
     * 事件发生时间
     */
    val timestamp: Instant

    /**
     * 事件类型
     */
    val eventType: EventType

    /**
     * 事件优先级
     */
    val priority: EventPriority
}

/**
 * 事件类型枚举
 */
enum class EventType {
    LOG_BROADCAST,
    PRE_NETWORK_SYNC,
    POST_NETWORK_SYNC,
    PRE_SYNC,
    POST_SYNC,
    CUSTOM_MESSAGE
}

/**
 * 事件优先级枚举
 */
enum class EventPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST
}

/**
 * 事件基础抽象类
 */
abstract class AbstractLuckPermsEvent(
    override val eventType: EventType,
    override val priority: EventPriority = EventPriority.NORMAL
) : LuckPermsEvent {
    override val eventId: String = java.util.UUID.randomUUID().toString()
    override val timestamp: Instant = Instant.now()
} 