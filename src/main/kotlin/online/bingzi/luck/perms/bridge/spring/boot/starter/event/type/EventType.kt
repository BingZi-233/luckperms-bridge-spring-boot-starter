package online.bingzi.luck.perms.bridge.spring.boot.starter.event.type

/**
 * 事件类型枚举
 */
enum class EventType {
    LOG_BROADCAST,
    PRE_NETWORK_SYNC,
    POST_NETWORK_SYNC,
    PRE_SYNC,
    POST_SYNC,
    CUSTOM_MESSAGE,
    CONNECTION_STATE
} 