package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.*
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryStrategy
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.retry.support.RetryTemplate
import okhttp3.sse.EventSources
import org.springframework.beans.factory.annotation.Qualifier
import online.bingzi.luck.perms.bridge.spring.boot.starter.config.EventSourceConfig

/**
 * SSE事件源工厂
 *
 * 负责创建和管理SSE事件监听器。此工厂类主要职责包括：
 * 1. 创建事件监听器
 * 2. 解析接收到的事件数据
 * 3. 将原始事件数据转换为对应的事件对象
 * 4. 管理重试机制
 *
 * @property objectMapper JSON解析器，用于解析事件数据
 * @property eventPublisher Spring事件发布器，用于发布解析后的事件
 * @property connectionStateProcessor 连接状态处理器，用于处理SSE连接的状态变化
 * @property retryStrategy SSE重试策略，用于管理连接重试
 * @property okHttpClient OkHttpClient实例，用于创建新的EventSource实例
 * @property eventSourceConfig EventSource配置，用于创建新的EventSource实例
 */
class EventSourceFactory(
    private val objectMapper: ObjectMapper,
    private val eventPublisher: ApplicationEventPublisher,
    private val connectionStateProcessor: ConnectionStateProcessor,
    private val retryStrategy: SSERetryStrategy,
    @Qualifier("sseOkHttpClient") private val okHttpClient: OkHttpClient,
    private val eventSourceConfig: EventSourceConfig
) {
    // 日志记录器
    private val logger = LoggerFactory.getLogger(EventSourceFactory::class.java)

    // 使用懒加载方式创建重试模板
    private val retryTemplate: RetryTemplate by lazy { retryStrategy.createRetryTemplate() }

    // 存储所有事件源的集合，以便管理
    private val eventSources = mutableMapOf<String, EventSource>()

    /**
     * 创建新的EventSource实例
     *
     * @param endpoint SSE端点URL，指定要连接的SSE服务地址
     * @return 新创建的EventSource实例，表示与SSE服务的连接
     */
    fun createEventSource(endpoint: String): EventSource {
        // 关闭旧的连接，如果存在
        eventSources[endpoint]?.cancel()
        eventSources.remove(endpoint)

        // 创建新的请求
        val request = Request.Builder()
            .url(endpoint) // 设置请求的URL
            .header("Accept", "text/event-stream") // 设置请求头，指定接受的内容类型
            .build()

        // 创建新的EventSource实例，并为其设置事件监听器
        val newEventSource = EventSources.createFactory(okHttpClient)
            .newEventSource(request, createListener(endpoint))
        
        // 将新创建的事件源存储到集合中
        eventSources[endpoint] = newEventSource
        return newEventSource
    }

    /**
     * 创建SSE事件监听器
     *
     * 创建一个新的事件监听器，用于处理指定端点的SSE事件。
     * 监听器会处理以下类型的事件：
     * - 连接状态变化（建立、关闭、失败）
     * - 日志广播事件
     * - 网络同步事件
     * - 本地同步事件
     * - 自定义消息事件
     * - ping事件（心跳，自动忽略）
     *
     * @param endpoint 要监听的SSE端点URL
     * @return 配置好的EventSourceListener实例，用于处理事件
     */
    fun createListener(endpoint: String): EventSourceListener {
        return object : EventSourceListener() {
            /**
             * 当连接成功建立时调用
             *
             * @param eventSource 当前的EventSource实例
             * @param response 连接成功后的响应
             */
            override fun onOpen(eventSource: EventSource, response: Response) {
                // 使用重试模板处理连接打开事件
                retryTemplate.execute<Unit, Throwable> {
                    connectionStateProcessor.handleConnectionOpen(eventSource, response, endpoint)
                }
            }

            /**
             * 当接收到事件时调用
             *
             * @param eventSource 当前的EventSource实例
             * @param id 可选的事件ID
             * @param type 事件类型
             * @param data 事件数据，包含事件的具体内容
             */
            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                // 忽略ping事件
                if (type == "ping") {
                    return
                }
                
                try {
                    // 根据事件类型解析事件数据
                    val event = when (type) {
                        "log-broadcast" -> {
                            val node = objectMapper.readTree(data)
                            LogBroadcastEvent(
                                source = eventSource,
                                message = node.get("message").asText(),
                                sourceType = node.get("sourceType").asText()
                            )
                        }
                        "pre-network-sync" -> {
                            val node = objectMapper.readTree(data)
                            PreNetworkSyncEvent(
                                source = eventSource,
                                sourceType = node.get("type").asText()
                            )
                        }
                        "post-network-sync" -> {
                            val node = objectMapper.readTree(data)
                            PostNetworkSyncEvent(
                                source = eventSource,
                                sourceType = node.get("type").asText(),
                                success = node.get("didSyncOccur").asBoolean()
                            )
                        }
                        "pre-sync" -> {
                            val node = objectMapper.readTree(data)
                            PreSyncEvent(
                                source = eventSource,
                                sourceType = node.get("cause").asText()
                            )
                        }
                        "post-sync" -> {
                            val node = objectMapper.readTree(data)
                            PostSyncEvent(
                                source = eventSource,
                                sourceType = node.get("cause").asText()
                            )
                        }
                        "custom-message" -> {
                            val node = objectMapper.readTree(data)
                            CustomMessageEvent(
                                source = eventSource,
                                channel = node.get("channel").asText(),
                                message = node.get("message").asText()
                            )
                        }
                        else -> {
                            // 记录未知事件类型的警告
                            logger.warn("未知的事件类型: {} - 订阅端点: {}", type, endpoint)
                            return
                        }
                    }

                    // 使用重试模板发布事件
                    retryTemplate.execute<Unit, Throwable> {
                        eventPublisher.publishEvent(event)
                    }
                } catch (e: Exception) {
                    // 处理事件时发生错误，记录错误信息
                    logger.error("处理事件时发生错误 - 订阅端点: {}", endpoint, e)
                }
            }

            /**
             * 当连接关闭时调用
             *
             * @param eventSource 当前的EventSource实例
             */
            override fun onClosed(eventSource: EventSource) {
                // 使用重试模板处理连接关闭事件
                retryTemplate.execute<Unit, Throwable> {
                    connectionStateProcessor.handleConnectionClosed(eventSource, endpoint)
                }
            }

            /**
             * 当连接失败时调用
             *
             * @param eventSource 当前的EventSource实例
             * @param t 发生的异常（如果有）
             * @param response 连接失败时的响应（如果有）
             */
            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                // 使用重试模板处理连接失败事件
                retryTemplate.execute<Unit, Throwable> {
                    connectionStateProcessor.handleConnectionFailure(eventSource, endpoint, t)
                }
            }
        }
    }
}