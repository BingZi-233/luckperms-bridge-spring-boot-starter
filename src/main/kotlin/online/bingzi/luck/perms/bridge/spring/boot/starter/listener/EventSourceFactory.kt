package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.bus.EventBus
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 事件源工厂
 * 负责创建事件监听器
 */
@Component
class EventSourceFactory(private val objectMapper: ObjectMapper) {

    private val logger = LoggerFactory.getLogger(EventSourceFactory::class.java)

    /**
     * 创建事件监听器
     * @param eventBus 事件总线
     * @return 事件监听器
     */
    fun createListener(eventBus: EventBus): EventSourceListener {
        return object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                logger.info("SSE连接已建立")
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                try {
                    val event = when (type) {
                        "log-broadcast" -> objectMapper.readValue(data, LogBroadcastEvent::class.java)
                        "pre-network-sync" -> objectMapper.readValue(data, PreNetworkSyncEvent::class.java)
                        "post-network-sync" -> objectMapper.readValue(data, PostNetworkSyncEvent::class.java)
                        "pre-sync" -> objectMapper.readValue(data, PreSyncEvent::class.java)
                        "post-sync" -> objectMapper.readValue(data, PostSyncEvent::class.java)
                        "custom-message" -> objectMapper.readValue(data, CustomMessageEvent::class.java)
                        else -> {
                            logger.warn("未知的事件类型: $type")
                            return
                        }
                    }
                    eventBus.publishEvent(event)
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