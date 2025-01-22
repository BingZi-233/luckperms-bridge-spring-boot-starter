package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.ConnectionStateHandler
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.EventSourceFactory
import online.bingzi.luck.perms.bridge.spring.boot.starter.manager.EventManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.support.RetryTemplate
import retrofit2.Retrofit

/**
 * 事件配置类
 * 负责配置事件相关的Bean
 */
@Configuration
class EventConfiguration {

    /**
     * 配置事件源工厂
     */
    @Bean
    @ConditionalOnMissingBean
    fun eventSourceFactory(
        objectMapper: ObjectMapper,
        eventPublisher: ApplicationEventPublisher,
        connectionStateHandler: ConnectionStateHandler
    ): EventSourceFactory {
        return EventSourceFactory(objectMapper, eventPublisher, connectionStateHandler)
    }

    /**
     * 配置事件管理器
     */
    @Bean
    @ConditionalOnMissingBean
    fun eventManager(
        retrofit: Retrofit,
        okHttpClient: OkHttpClient,
        retryTemplate: RetryTemplate,
        eventSourceFactory: EventSourceFactory
    ): EventManager {
        return EventManager(retrofit, okHttpClient, retryTemplate, eventSourceFactory)
    }
} 