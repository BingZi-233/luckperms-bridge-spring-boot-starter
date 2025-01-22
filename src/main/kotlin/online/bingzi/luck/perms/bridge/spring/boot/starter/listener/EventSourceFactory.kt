package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.*
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher

/**
 * 事件源工厂
 * 负责创建事件监听器
 */
class EventSourceFactory(
    private val objectMapper: ObjectMapper,
    private val eventPublisher: ApplicationEventPublisher
) {

    private val logger = LoggerFactory.getLogger(EventSourceFactory::class.java)

    /**
     * 创建事件监听器
     * @return 事件监听器
     */
    fun createListener(): EventSourceListener {
        return object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                logger.info("SSE连接已建立")
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                try {
                    val event = when (type) {
                        "log-broadcast" -> objectMapper.readValue(data, Map::class.java).let {
                            LogBroadcastEvent(
                                source = eventSource,
                                message = it["message"] as String,
                                sourceType = it["source"] as String
                            )
                        }
                        "pre-network-sync" -> objectMapper.readValue(data, Map::class.java).let {
                            PreNetworkSyncEvent(
                                source = eventSource,
                                syncId = it["syncId"] as String,
                                syncType = it["type"] as String
                            )
                        }
                        "post-network-sync" -> objectMapper.readValue(data, Map::class.java).let {
                            PostNetworkSyncEvent(
                                source = eventSource,
                                syncId = it["syncId"] as String,
                                syncType = it["type"] as String,
                                didSyncOccur = it["didSyncOccur"] as Boolean
                            )
                        }
                        "pre-sync" -> objectMapper.readValue(data, Map::class.java).let {
                            PreSyncEvent(
                                source = eventSource,
                                cause = it["cause"] as String
                            )
                        }
                        "post-sync" -> objectMapper.readValue(data, Map::class.java).let {
                            PostSyncEvent(
                                source = eventSource,
                                cause = it["cause"] as String,
                                didSyncOccur = it["didSyncOccur"] as Boolean
                            )
                        }
                        "custom-message" -> objectMapper.readValue(data, Map::class.java).let {
                            CustomMessageEvent(
                                source = eventSource,
                                channel = it["channel"] as String,
                                message = it["message"] as String
                            )
                        }
                        else -> {
                            logger.warn("未知的事件类型: $type")
                            return
                        }
                    }
                    eventPublisher.publishEvent(event)
                } catch (e: Exception) {
                    logger.error("处理事件时发生错误", e)
                }
            }

            override fun onClosed(eventSource: EventSource) {
                logger.info("SSE连接已关闭")
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                logger.error("SSE连接失败", t)
            }
        }
    }
} 