package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import org.springframework.context.ApplicationEvent
import java.time.Instant

/**
 * LuckPerms事件基础类
 * 所有LuckPerms事件都必须继承此类
 */
abstract class LuckPermsEvent(
    source: Any,
    val eventType: EventType,
    val priority: EventPriority = EventPriority.NORMAL
) : ApplicationEvent(source) {
    val eventId: String = java.util.UUID.randomUUID().toString()
    val timestamp: Instant = Instant.now()
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