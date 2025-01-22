package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.state

/**
 * SSE连接状态枚举
 *
 * 定义了SSE连接可能的状态：
 * - CONNECTED: 连接已成功建立
 * - CLOSED: 连接已正常关闭
 * - FAILED: 连接发生错误或失败
 */
enum class ConnectionState {
    /** 连接已建立 */
    CONNECTED,
    /** 连接已关闭 */
    CLOSED,
    /** 连接失败 */
    FAILED
} 