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
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

/**
 * 事件管理器
 * 负责管理LuckPerms事件的订阅生命周期
 */
@Component
class EventManager(
    private val retrofit: Retrofit,
    private val okHttpClient: OkHttpClient,
    private val retryTemplate: RetryTemplate
) : InitializingBean, DisposableBean {

    private val logger = LoggerFactory.getLogger(EventManager::class.java)
    private val eventSources = ConcurrentHashMap<Request, EventSource>()
    private val eventApi = retrofit.create(EventApi::class.java)
    private lateinit var eventListener: EventListener

    fun setEventListener(listener: EventListener) {
        this.eventListener = listener
    }

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
        eventSources.values.forEach { it.cancel() }
        eventSources.clear()
    }

    /**
     * 处理连接失败的情况
     * @param failedEventSource 失败的EventSource
     */
    fun handleConnectionFailure(failedEventSource: EventSource) {
        val request = eventSources.entries.find { it.value == failedEventSource }?.key
        if (request != null) {
            failedEventSource.cancel() // 关闭失败的连接
            eventSources.remove(request) // 从映射中移除
            subscribeEventsWithRetry(request) // 重新订阅
        }
    }

    private fun subscribeLogBroadcastEvents() {
        val request = eventApi.subscribeLogBroadcastEvents().request()
        subscribeEventsWithRetry(request)
    }

    private fun subscribeNetworkSyncEvents() {
        // 订阅网络同步前事件
        val preRequest = eventApi.subscribePreNetworkSyncEvents().request()
        subscribeEventsWithRetry(preRequest)

        // 订阅网络同步后事件
        val postRequest = eventApi.subscribePostNetworkSyncEvents().request()
        subscribeEventsWithRetry(postRequest)
    }

    private fun subscribeSyncEvents() {
        // 订阅同步前事件
        val preRequest = eventApi.subscribePreSyncEvents().request()
        subscribeEventsWithRetry(preRequest)

        // 订阅同步后事件
        val postRequest = eventApi.subscribePostSyncEvents().request()
        subscribeEventsWithRetry(postRequest)
    }

    private fun subscribeCustomMessageEvents() {
        val request = eventApi.subscribeCustomMessageReceiveEvents().request()
        subscribeEventsWithRetry(request)
    }

    private fun subscribeEventsWithRetry(request: Request) {
        retryTemplate.execute<Unit, Exception> { context ->
            try {
                val eventSource = EventSources.createFactory(okHttpClient)
                    .newEventSource(request, eventListener as EventSourceListener)
                eventSources[request] = eventSource
                logger.info("已成功订阅事件: ${request.url}, 重试次数: ${context.retryCount}")
            } catch (e: Exception) {
                logger.error("订阅事件失败: ${request.url}, 重试次数: ${context.retryCount}", e)
                throw e
            }
        }
    }
} 