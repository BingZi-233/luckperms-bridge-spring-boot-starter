package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums

/**
 * SSE连接状态枚举
 * 该枚举用于表示服务器推送事件（SSE）连接的不同状态。
 * 主要功能是定义SSE连接的各个可能状态，便于在程序中管理和判断连接的状况。
 */
enum class ConnectionStateType {
    /**
     * 已连接
     * 表示SSE连接已成功建立并正常工作，客户端可以接收事件。
     */
    CONNECTED,

    /**
     * 已断开
     * 表示SSE连接已断开，可能是正常关闭（如用户主动断开）或异常断开（如网络问题）。
     */
    DISCONNECTED,

    /**
     * 正在连接
     * 表示SSE连接正在尝试建立连接，客户端正在发起连接请求。
     */
    CONNECTING,

    /**
     * 正在重试
     * 表示SSE连接失败后，客户端正在进行重试操作以重新建立连接。
     */
    RETRYING,

    /**
     * 已暂停
     * 表示SSE连接被用户或系统手动暂停，客户端暂时不接收事件。
     */
    SUSPENDED,

    /**
     * 已失败
     * 表示SSE连接失败且无法恢复，可能需要用户手动重启连接。
     */
    FAILED,

    /**
     * 已关闭
     * 表示SSE连接已被主动关闭，客户端不再接收事件。
     */
    CLOSED,

    /**
     * 未知
     * 表示SSE连接状态无法确定，可能由于内部错误或状态未定义。
     */
    UNKNOWN;

    /**
     * 检查是否处于已连接状态
     * 该方法用于判断当前连接状态是否为已连接（CONNECTED）。
     * @return 如果当前状态为CONNECTED，返回true；否则返回false。
     */
    fun isConnected(): Boolean = this == CONNECTED
}