package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType
import org.springframework.context.ApplicationEvent
import java.time.Instant

/**
 * LuckPerms事件基础类
 * 所有LuckPerms事件都必须继承此类
 */
abstract class LuckPermsEvent(
    source: EventSource,
    val eventType: EventType,
    val priority: EventPriority = EventPriority.NORMAL
) : ApplicationEvent(source) {
    val eventId: String = java.util.UUID.randomUUID().toString()
    val timestamp: Instant = Instant.now()

    /**
     * 获取事件源
     * @return EventSource对象
     */
    fun getEventSource(): EventSource = source as EventSource
}