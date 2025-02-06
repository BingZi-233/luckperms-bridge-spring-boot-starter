package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.*
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryStrategy
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.retry.support.RetryTemplate

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
 * @property connectionStateHandler 连接状态处理器，用于处理SSE连接的状态变化
 * @property retryStrategy SSE重试策略，用于管理连接重试
 */
class EventSourceFactory(
    private val objectMapper: ObjectMapper,
    private val eventPublisher: ApplicationEventPublisher,
    private val connectionStateHandler: ConnectionStateHandler,
    private val retryStrategy: SSERetryStrategy
) {
    private val logger = LoggerFactory.getLogger(EventSourceFactory::class.java)
    private val retryTemplate: RetryTemplate by lazy { retryStrategy.createRetryTemplate() }

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
     * @return 配置好的EventSourceListener实例
     */
    fun createListener(endpoint: String): EventSourceListener {
        return object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                retryTemplate.execute<Unit, Throwable> {
                    connectionStateHandler.handleConnectionOpen(eventSource, response, endpoint)
                }
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                // 忽略ping事件
                if (type == "ping") {
                    return
                }
                
                try {
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
                            logger.warn("未知的事件类型: {} - 订阅端点: {}", type, endpoint)
                            return
                        }
                    }
                    retryTemplate.execute<Unit, Throwable> {
                        eventPublisher.publishEvent(event)
                    }
                } catch (e: Exception) {
                    logger.error("处理事件时发生错误 - 订阅端点: {}", endpoint, e)
                }
            }

            override fun onClosed(eventSource: EventSource) {
                retryTemplate.execute<Unit, Throwable> {
                    connectionStateHandler.handleConnectionClosed(eventSource, endpoint)
                }
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                retryTemplate.execute<Unit, Throwable> {
                    connectionStateHandler.handleConnectionFailure(eventSource, endpoint, t)
                }
            }
        }
    }
} 