package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.state

/**
 * SSE连接状态枚举
 *
 * 定义了SSE连接可能的状态：
 * - CONNECTED: 连接已成功建立
 * - DISCONNECTED: 连接已断开
 * - CONNECTING: 正在尝试连接
 */
enum class ConnectionState {
    /** 连接已建立 */
    CONNECTED,
    /** 连接已断开 */
    DISCONNECTED,
    /** 正在连接 */
    CONNECTING
} 