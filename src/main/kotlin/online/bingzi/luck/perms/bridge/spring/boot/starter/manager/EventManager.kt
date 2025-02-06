package online.bingzi.luck.perms.bridge.spring.boot.starter.manager

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSources
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.EventApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.EventSourceFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.retry.support.RetryTemplate
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap

/**
 * 事件管理器
 * 该类负责管理LuckPerms事件的订阅生命周期，包括事件的订阅、处理连接失败的情况以及在Bean的初始化与销毁阶段进行相应的操作。
 */
class EventManager(
    private val retrofit: Retrofit, // Retrofit实例，用于创建API接口
    private val okHttpClient: OkHttpClient, // OkHttpClient实例，用于网络请求
    private val retryTemplate: RetryTemplate, // 重试模板，用于处理请求失败后的重试逻辑
    private val eventSourceFactory: EventSourceFactory // 事件源工厂，用于创建事件监听器
) : InitializingBean, DisposableBean {

    private val logger = LoggerFactory.getLogger(EventManager::class.java) // 日志记录器
    private val eventSources = ConcurrentHashMap<Request, EventSource>() // 存储当前所有的事件源
    private val eventApi = retrofit.create(EventApi::class.java) // 创建事件API接口的实例

    /**
     * 在Bean初始化完成后自动订阅所有事件
     * 该方法会在Spring容器初始化该Bean后被调用，负责触发所有事件的订阅。
     */
    override fun afterPropertiesSet() {
        subscribeAllEvents() // 调用订阅所有事件的方法
    }

    /**
     * 在Bean销毁前取消所有事件订阅
     * 该方法会在Spring容器销毁该Bean前被调用，负责清理所有的事件源。
     */
    override fun destroy() {
        // 遍历所有事件源并取消订阅
        eventSources.values.forEach { it.cancel() }
        eventSources.clear() // 清空事件源的集合
    }

    /**
     * 处理连接失败的情况
     * @param failedEventSource 失败的EventSource，发生连接失败的事件源
     * 该方法会从当前事件源中查找并移除失败的事件源，然后尝试重新订阅该事件。
     */
    fun handleConnectionFailure(failedEventSource: EventSource) {
        // 查找与失败的事件源对应的请求
        val request = eventSources.entries.find { it.value == failedEventSource }?.key
        if (request != null) {
            failedEventSource.cancel() // 取消失败的事件源
            eventSources.remove(request) // 从事件源集合中移除该请求
            subscribeEventsWithRetry(request) // 尝试重新订阅该事件
        }
    }

    /**
     * 订阅所有事件
     * 该私有方法会调用事件API获取所有事件的请求并逐个进行订阅。
     */
    private fun subscribeAllEvents() {
        // 获取所有事件的请求
        val requests = listOf(
            eventApi.subscribeLogBroadcastEvents(),
            eventApi.subscribePreNetworkSyncEvents(),
            eventApi.subscribePostNetworkSyncEvents(),
            eventApi.subscribePreSyncEvents(),
            eventApi.subscribePostSyncEvents(),
            eventApi.subscribeCustomMessageReceiveEvents()
        ).map { it.request() } // 转换为请求列表

        // 遍历每个请求并进行订阅
        requests.forEach { subscribeEventsWithRetry(it) }
    }

    /**
     * 尝试订阅事件并处理重试逻辑
     * @param request 事件请求，包含订阅事件所需的请求信息
     * 该私有方法会使用重试模板来处理可能的订阅失败情况，并记录相关日志。
     */
    private fun subscribeEventsWithRetry(request: Request) {
        retryTemplate.execute<Unit, Exception> { context -> // 执行重试逻辑
            try {
                // 创建事件源并开始订阅
                val eventSource = EventSources.createFactory(okHttpClient)
                    .newEventSource(request, eventSourceFactory.createListener(request.url.toString()))
                eventSources[request] = eventSource // 将事件源与请求存储在集合中
                // 根据重试次数记录不同的日志
                if (context.retryCount > 0) {
                    logger.info("已成功订阅事件: ${request.url}, 重试次数: ${context.retryCount}") // 记录重试成功的日志
                } else {
                    logger.info("已成功订阅事件: ${request.url}") // 记录首次成功的日志
                }
            } catch (e: Exception) {
                logger.error("订阅事件失败: ${request.url}, 重试次数: ${context.retryCount}", e) // 记录订阅失败的日志
                throw e // 抛出异常以触发重试
            }
        }
    }
}