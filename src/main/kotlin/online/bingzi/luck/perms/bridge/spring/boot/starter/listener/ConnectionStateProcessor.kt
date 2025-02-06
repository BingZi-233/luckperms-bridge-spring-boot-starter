package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import okhttp3.Response
import okhttp3.sse.EventSource

/**
 * SSE连接状态处理器接口
 */
interface ConnectionStateProcessor {
    /**
     * 处理连接建立事件
     */
    fun handleConnectionOpen(eventSource: EventSource, response: Response, endpoint: String)

    /**
     * 处理连接关闭事件
     */
    fun handleConnectionClosed(eventSource: EventSource, endpoint: String)

    /**
     * 处理连接失败事件
     */
    fun handleConnectionFailure(eventSource: EventSource, endpoint: String, error: Throwable?)
} 