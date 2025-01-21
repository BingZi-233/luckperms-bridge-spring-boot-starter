package online.bingzi.luck.perms.bridge.spring.boot.starter.event.bus

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.handler.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 事件总线
 * 负责事件的分发和处理器的管理
 */
@Component
class EventBus {
    private val logger = LoggerFactory.getLogger(EventBus::class.java)
    private val handlers = ConcurrentHashMap<Class<out LuckPermsEvent>, CopyOnWriteArrayList<EventHandler<*>>>()

    /**
     * 注册事件处理器
     * @param handler 要注册的事件处理器
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : LuckPermsEvent> registerHandler(handler: EventHandler<T>) {
        val eventType = handler.getSupportedEventType()
        handlers.computeIfAbsent(eventType) { CopyOnWriteArrayList() }
            .add(handler as EventHandler<*>)
        logger.info("已注册事件处理器: ${handler.javaClass.simpleName} 用于事件类型: ${eventType.simpleName}")
    }

    /**
     * 注销事件处理器
     * @param handler 要注销的事件处理器
     */
    fun <T : LuckPermsEvent> unregisterHandler(handler: EventHandler<T>) {
        val eventType = handler.getSupportedEventType()
        handlers[eventType]?.remove(handler)
        logger.info("已注销事件处理器: ${handler.javaClass.simpleName} 用于事件类型: ${eventType.simpleName}")
    }

    /**
     * 发布事件
     * @param event 要发布的事件
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : LuckPermsEvent> publishEvent(event: T) {
        val eventType = event.javaClass
        handlers[eventType]?.forEach { handler ->
            try {
                (handler as EventHandler<T>).handle(event)
            } catch (e: Exception) {
                logger.error("处理事件时发生错误: ${e.message}", e)
            }
        }
    }

    /**
     * 清除所有事件处理器
     */
    fun clearHandlers() {
        handlers.clear()
        logger.info("已清除所有事件处理器")
    }
} 