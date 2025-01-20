package online.bingzi.luck.perms.bridge.spring.boot.starter.listener

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * LuckPerms事件监听器
 */
@Component
class EventListener : EventSourceListener() {
    private val logger = LoggerFactory.getLogger(EventListener::class.java)
    private val objectMapper = ObjectMapper()

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
                else -> {
                    logger.warn("未知的事件类型: $type")
                    return
                }
            }
            handleEvent(event)
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

    private fun handleEvent(event: LuckPermsEvent) {
        when (event) {
            is LogBroadcastEvent -> logger.info("收到日志广播: ${event.message}")
            is PreNetworkSyncEvent -> logger.info("收到网络同步前事件: syncId=${event.syncId}, type=${event.syncType}")
            is PostNetworkSyncEvent -> logger.info("收到网络同步后事件: syncId=${event.syncId}, type=${event.syncType}, didSyncOccur=${event.didSyncOccur}")
            is PreSyncEvent -> logger.info("收到同步前事件")
            is PostSyncEvent -> logger.info("收到同步后事件")
            else -> logger.info("收到未知事件: ${event.getType()}")
        }
    }
} 