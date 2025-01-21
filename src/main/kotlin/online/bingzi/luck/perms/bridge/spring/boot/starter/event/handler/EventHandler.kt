package online.bingzi.luck.perms.bridge.spring.boot.starter.event.handler

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent

/**
 * 事件处理器接口
 */
interface EventHandler<T : LuckPermsEvent> {
    /**
     * 处理事件
     * @param event 要处理的事件
     */
    fun handle(event: T)

    /**
     * 获取处理器支持的事件类型
     * @return 事件类型的Class对象
     */
    fun getSupportedEventType(): Class<T>
} 