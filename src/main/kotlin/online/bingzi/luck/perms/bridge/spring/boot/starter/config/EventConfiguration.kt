package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.ConnectionStateHandler
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.EventSourceFactory
import online.bingzi.luck.perms.bridge.spring.boot.starter.manager.EventManager
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryStrategy
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.support.RetryTemplate
import retrofit2.Retrofit
import org.springframework.beans.factory.annotation.Qualifier
import online.bingzi.luck.perms.bridge.spring.boot.starter.config.EventSourceConfig
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.ConnectionStateProcessor

/**
 * 事件配置类
 * 负责配置事件相关的Bean
 */
@Configuration
@ConditionalOnProperty(prefix = "luck-perms", name = ["enabled"], matchIfMissing = true)
class EventConfiguration {

    /**
     * 配置事件源工厂
     * @param objectMapper 用于将Java对象转换为JSON格式的ObjectMapper实例
     * @param eventPublisher Spring的事件发布器，用于发布事件
     * @param connectionStateHandler 处理连接状态的监听器
     * @param retryStrategy SSE重试策略
     * @param okHttpClient 用于HTTP请求的OkHttpClient实例，使用@Qualifier注解指定
     * @param eventSourceConfig 事件源的配置
     * @return 返回一个EventSourceFactory实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun eventSourceFactory(
        objectMapper: ObjectMapper,
        eventPublisher: ApplicationEventPublisher,
        connectionStateHandler: ConnectionStateProcessor,
        retryStrategy: SSERetryStrategy,
        @Qualifier("sseOkHttpClient") okHttpClient: OkHttpClient,
        eventSourceConfig: EventSourceConfig
    ): EventSourceFactory {
        // 创建并返回一个新的EventSourceFactory实例
        return EventSourceFactory(
            objectMapper,
            eventPublisher,
            connectionStateHandler,
            retryStrategy,
            okHttpClient,
            eventSourceConfig
        )
    }

    /**
     * 配置事件管理器
     * @param retrofit Retrofit实例，用于网络请求
     * @param okHttpClient 用于HTTP请求的OkHttpClient实例
     * @param retryTemplate Spring Retry模板，用于重试机制
     * @param eventSourceFactory 事件源工厂的实例
     * @return 返回一个EventManager实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun eventManager(
        retrofit: Retrofit,
        okHttpClient: OkHttpClient,
        retryTemplate: RetryTemplate,
        eventSourceFactory: EventSourceFactory
    ): EventManager {
        // 创建并返回一个新的EventManager实例
        return EventManager(retrofit, okHttpClient, retryTemplate, eventSourceFactory)
    }
}