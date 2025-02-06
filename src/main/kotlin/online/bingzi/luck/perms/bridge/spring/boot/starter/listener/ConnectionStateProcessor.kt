package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import okhttp3.Response
import okhttp3.sse.EventSource

/**
 * SSE连接状态处理器接口
 * 
 * 该接口定义了处理SSE（Server-Sent Events）连接状态的基本方法， 
 * 包含连接建立、关闭和失败事件的处理逻辑。实现此接口的类
 * 需要提供具体的事件处理机制，用于监控和响应SSE连接的状态变化。
 */
interface ConnectionStateProcessor {
    
    /**
     * 处理连接建立事件
     * 
     * 当与SSE服务器成功建立连接时调用此方法。
     * 
     * @param eventSource 代表当前的SSE连接源，类型为EventSource。
     * @param response 服务器响应，类型为Response，包含连接建立时的HTTP响应信息。
     * @param endpoint 连接的终端点地址，类型为String，指示SSE的URL。
     */
    fun handleConnectionOpen(eventSource: EventSource, response: Response, endpoint: String)

    /**
     * 处理连接关闭事件
     * 
     * 当与SSE服务器的连接正常关闭时调用此方法。
     * 
     * @param eventSource 代表当前的SSE连接源，类型为EventSource。
     * @param endpoint 连接的终端点地址，类型为String，指示SSE的URL。
     */
    fun handleConnectionClosed(eventSource: EventSource, endpoint: String)

    /**
     * 处理连接失败事件
     * 
     * 当与SSE服务器的连接出现错误或失败时调用此方法。
     * 
     * @param eventSource 代表当前的SSE连接源，类型为EventSource。
     * @param endpoint 连接的终端点地址，类型为String，指示SSE的URL。
     * @param error 连接失败的异常信息，类型为Throwable?，可以为null，表示未发生错误。
     */
    fun handleConnectionFailure(eventSource: EventSource, endpoint: String, error: Throwable?)
}