package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType
import org.springframework.context.ApplicationEvent
import java.time.Instant

/**
 * LuckPerms事件基础类
 * 所有LuckPerms事件都必须继承此类，提供事件的基本属性和功能。
 * 包含事件的类型、优先级、ID和时间戳等信息。
 */
abstract class LuckPermsEvent(
    source: EventSource, // 事件源，表示事件的来源
    val eventType: EventType, // 事件类型，表示事件的具体类型
    val priority: EventPriority = EventPriority.NORMAL // 事件优先级，默认为NORMAL级别
) : ApplicationEvent(source) { // 继承自Spring的ApplicationEvent类
    // 事件的唯一标识符，使用UUID生成
    val eventId: String = java.util.UUID.randomUUID().toString()
    
    // 事件发生的时间戳，表示事件被创建的时间
    val timestamp: Instant = Instant.now()

    /**
     * 获取事件源
     * @return 返回事件源的EventSource对象
     */
    fun getEventSource(): EventSource = source as EventSource // 返回源对象并类型转换为EventSource
}