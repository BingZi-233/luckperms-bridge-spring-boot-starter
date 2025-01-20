package online.bingzi.luck.perms.bridge.spring.boot.starter.manager

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.EventApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.EventListener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import retrofit2.Retrofit

/**
 * 事件管理器
 * 负责管理LuckPerms事件的订阅生命周期
 */
@Component
class EventManager(
    private val retrofit: Retrofit,
    private val okHttpClient: OkHttpClient
) : InitializingBean, DisposableBean {

    private val logger = LoggerFactory.getLogger(EventManager::class.java)
    private val eventSources = mutableListOf<EventSource>()
    private val eventApi = retrofit.create(EventApi::class.java)
    private val eventListener = EventListener()

    /**
     * 在Bean初始化完成后自动订阅所有事件
     */
    override fun afterPropertiesSet() {
        subscribeLogBroadcastEvents()
        subscribeNetworkSyncEvents()
        subscribeSyncEvents()
        subscribeCustomMessageEvents()
    }

    /**
     * 在Bean销毁前取消所有事件订阅
     */
    override fun destroy() {
        eventSources.forEach { it.cancel() }
        eventSources.clear()
    }

    private fun subscribeLogBroadcastEvents() {
        val request = eventApi.subscribeLogBroadcastEvents().request()
        subscribeEvents(request)
    }

    private fun subscribeNetworkSyncEvents() {
        // 订阅网络同步前事件
        val preRequest = eventApi.subscribePreNetworkSyncEvents().request()
        subscribeEvents(preRequest)

        // 订阅网络同步后事件
        val postRequest = eventApi.subscribePostNetworkSyncEvents().request()
        subscribeEvents(postRequest)
    }

    private fun subscribeSyncEvents() {
        // 订阅同步前事件
        val preRequest = eventApi.subscribePreSyncEvents().request()
        subscribeEvents(preRequest)

        // 订阅同步后事件
        val postRequest = eventApi.subscribePostSyncEvents().request()
        subscribeEvents(postRequest)
    }

    private fun subscribeCustomMessageEvents() {
        val request = eventApi.subscribeCustomMessageReceiveEvents().request()
        subscribeEvents(request)
    }

    private fun subscribeEvents(request: Request) {
        val eventSource = EventSources.createFactory(okHttpClient)
            .newEventSource(request, eventListener as EventSourceListener)
        eventSources.add(eventSource)
        logger.info("已订阅事件: ${request.url}")
    }
} 