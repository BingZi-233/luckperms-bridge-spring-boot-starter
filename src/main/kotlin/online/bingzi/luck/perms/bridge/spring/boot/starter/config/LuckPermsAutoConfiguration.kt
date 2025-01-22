package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.HealthApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.MessagingApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.aspect.ContextAspect
import online.bingzi.luck.perms.bridge.spring.boot.starter.aspect.GroupAspect
import online.bingzi.luck.perms.bridge.spring.boot.starter.aspect.PermissionAspect
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.ConnectionStateHandler
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.EventSourceFactory
import online.bingzi.luck.perms.bridge.spring.boot.starter.manager.EventManager
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.ContextService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.GroupService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.PermissionService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.DefaultUserIdentityService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsContextService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsGroupService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsPermissionService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * LuckPerms自动配置类
 */
@Configuration
@EnableAspectJAutoProxy
@EnableRetry
@EnableConfigurationProperties(LuckPermsProperties::class, RetryProperties::class)
@ConditionalOnProperty(prefix = "luck-perms", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@ComponentScan("online.bingzi.luck.perms.bridge.spring.boot.starter")
class LuckPermsAutoConfiguration(
    private val properties: LuckPermsProperties,
    private val retryProperties: RetryProperties
) {

    /**
     * 配置RetryTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    fun retryTemplate(): RetryTemplate {
        return RetryTemplate().apply {
            setBackOffPolicy(ExponentialBackOffPolicy().apply {
                initialInterval = retryProperties.initialInterval
                multiplier = retryProperties.multiplier
                maxInterval = retryProperties.maxInterval
            })
            
            setRetryPolicy(SimpleRetryPolicy().apply {
                maxAttempts = retryProperties.maxAttempts
            })
        }
    }

    /**
     * 配置EventSourceFactory
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
     * 配置EventManager
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

    /**
     * 配置ObjectMapper
     */
    @Bean
    @ConditionalOnMissingBean
    fun objectMapper(): ObjectMapper = ObjectMapper()

    /**
     * 配置OkHttpClient
     */
    @Bean
    @ConditionalOnMissingBean
    fun okHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", properties.apiKey)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * 配置Retrofit
     */
    @Bean
    @ConditionalOnMissingBean
    fun retrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(properties.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }

    /**
     * 配置UserApi
     */
    @Bean
    @ConditionalOnMissingBean
    fun userApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    /**
     * 配置MessagingApi
     */
    @Bean
    @ConditionalOnMissingBean
    fun messagingApi(retrofit: Retrofit): MessagingApi {
        return retrofit.create(MessagingApi::class.java)
    }

    /**
     * 配置HealthApi
     */
    @Bean
    @ConditionalOnMissingBean
    fun healthApi(retrofit: Retrofit): HealthApi {
        return retrofit.create(HealthApi::class.java)
    }

    /**
     * 配置默认的UserIdentityService
     */
    @Bean
    @ConditionalOnMissingBean(UserIdentityService::class)
    fun defaultUserIdentityService(): UserIdentityService {
        return DefaultUserIdentityService()
    }

    /**
     * 配置PermissionService
     */
    @Bean
    @ConditionalOnMissingBean
    fun permissionService(userApi: UserApi): PermissionService {
        return LuckPermsPermissionService(userApi)
    }

    /**
     * 配置GroupService
     */
    @Bean
    @ConditionalOnMissingBean
    fun groupService(userApi: UserApi): GroupService {
        return LuckPermsGroupService(userApi)
    }

    /**
     * 配置ContextService
     */
    @Bean
    @ConditionalOnMissingBean
    fun contextService(userApi: UserApi): ContextService {
        return LuckPermsContextService(userApi)
    }

    /**
     * 配置PermissionAspect
     */
    @Bean
    @ConditionalOnMissingBean
    fun permissionAspect(
        permissionService: PermissionService,
        userIdentityService: UserIdentityService
    ): PermissionAspect {
        return PermissionAspect(permissionService, userIdentityService)
    }

    /**
     * 配置GroupAspect
     */
    @Bean
    @ConditionalOnMissingBean
    fun groupAspect(
        groupService: GroupService,
        userIdentityService: UserIdentityService
    ): GroupAspect {
        return GroupAspect(groupService, userIdentityService)
    }

    /**
     * 配置ContextAspect
     */
    @Bean
    @ConditionalOnMissingBean
    fun contextAspect(
        contextService: ContextService,
        userIdentityService: UserIdentityService
    ): ContextAspect {
        return ContextAspect(contextService, userIdentityService)
    }
} 