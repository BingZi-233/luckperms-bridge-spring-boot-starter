package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums

/**
 * SSE连接状态枚举
 * 用于表示SSE连接的不同状态
 */
enum class ConnectionStateType {
    /**
     * 已连接
     * 表示SSE连接已成功建立并正常工作
     */
    CONNECTED,

    /**
     * 已断开
     * 表示SSE连接已断开，可能是正常关闭或异常断开
     */
    DISCONNECTED,

    /**
     * 正在连接
     * 表示SSE连接正在尝试建立连接
     */
    CONNECTING,

    /**
     * 正在重试
     * 表示SSE连接失败后正在进行重试
     */
    RETRYING,

    /**
     * 已暂停
     * 表示SSE连接被手动暂停
     */
    SUSPENDED,

    /**
     * 已失败
     * 表示SSE连接失败且无法恢复
     */
    FAILED,

    /**
     * 已关闭
     * 表示SSE连接已被主动关闭
     */
    CLOSED,

    /**
     * 未知
     * 表示SSE连接状态无法确定
     */
    UNKNOWN;

    /**
     * 检查是否处于已连接状态
     * 只有CONNECTED状态被认为是已连接
     */
    fun isConnected(): Boolean = this == CONNECTED
} 