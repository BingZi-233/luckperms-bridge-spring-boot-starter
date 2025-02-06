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
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.ConnectionStateProcessor
import online.bingzi.luck.perms.bridge.spring.boot.starter.listener.EventSourceFactory
import online.bingzi.luck.perms.bridge.spring.boot.starter.manager.EventManager
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSERetryStrategy
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.ContextService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.GroupService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.PermissionService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.DefaultUserIdentityService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsContextService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsGroupService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsPermissionService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.*
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.annotation.RetryConfiguration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * LuckPerms自动配置类
 * 该类负责LuckPerms的自动配置，包括各种服务、API和相关的组件的初始化。
 */
@Configuration
@EnableAspectJAutoProxy
@EnableRetry
@EnableConfigurationProperties(LuckPermsProperties::class, RetryProperties::class, HealthCheckProperties::class, EventSourceConfig::class)
@ComponentScan("online.bingzi.luck.perms.bridge.spring.boot.starter")
@Import(RetryConfiguration::class)
@ConditionalOnProperty(prefix = "luck-perms", name = ["enabled"], matchIfMissing = true)
class LuckPermsAutoConfiguration(
    private val properties: LuckPermsProperties, // LuckPerms配置属性
    private val retryProperties: RetryProperties, // 重试配置属性
    private val eventSourceConfig: EventSourceConfig // 事件源配置属性
) {

    /**
     * 配置SSE重试策略
     * @return 返回配置好的SSERetryStrategy实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun sseRetryStrategy(): SSERetryStrategy {
        return SSERetryStrategy(
            maxAttempts = retryProperties.maxAttempts, // 最大重试次数
            initialInterval = retryProperties.initialInterval, // 初始重试间隔
            multiplier = retryProperties.multiplier, // 重试间隔的乘数
            maxInterval = retryProperties.maxInterval // 最大重试间隔
        )
    }

    /**
     * 配置RetryTemplate
     * @return 返回配置好的RetryTemplate实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun retryTemplate(): RetryTemplate {
        return RetryTemplate().apply {
            // 配置指数退避策略
            setBackOffPolicy(ExponentialBackOffPolicy().apply {
                initialInterval = retryProperties.initialInterval // 初始间隔
                multiplier = retryProperties.multiplier // 乘数
                maxInterval = retryProperties.maxInterval // 最大间隔
            })
            
            // 配置简单重试策略
            setRetryPolicy(SimpleRetryPolicy().apply {
                maxAttempts = retryProperties.maxAttempts // 最大尝试次数
            })
        }
    }

    /**
     * 配置EventSourceFactory
     * @param objectMapper 用于序列化和反序列化的ObjectMapper实例
     * @param eventPublisher 事件发布器
     * @param connectionStateHandler 连接状态处理器
     * @param retryStrategy SSE重试策略
     * @param okHttpClient OkHttp客户端
     * @param eventSourceConfig 事件源配置
     * @return 返回配置好的EventSourceFactory实例
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
     * 配置EventManager
     * @param retrofit Retrofit实例
     * @param okHttpClient OkHttp客户端
     * @param retryTemplate 重试模板
     * @param eventSourceFactory 事件源工厂
     * @return 返回配置好的EventManager实例
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
     * @return 返回默认的ObjectMapper实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun objectMapper(): ObjectMapper = ObjectMapper()

    /**
     * 配置OkHttpClient
     * @return 返回配置好的OkHttpClient实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun okHttpClient(): OkHttpClient {
        // 创建并配置日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC // 设置日志级别为基本
        }

        // 构建OkHttpClient
        return OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS) // 连接超时设置
            .readTimeout(5000, TimeUnit.MILLISECONDS) // 读取超时设置
            .writeTimeout(5000, TimeUnit.MILLISECONDS) // 写入超时设置
            .addInterceptor { chain -> // 添加请求拦截器
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", properties.apiKey) // 添加Authorization头
                    .build()
                chain.proceed(request) // 继续处理请求
            }
            .addInterceptor(loggingInterceptor) // 添加日志拦截器
            .build() // 构建OkHttpClient
    }

    /**
     * 配置用于SSE的OkHttpClient
     * @return 返回配置好的SSE OkHttpClient实例
     */
    @Bean
    @Qualifier("sseOkHttpClient")
    fun sseOkHttpClient(): OkHttpClient {
        // 创建并配置日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC // 设置日志级别为基本
        }

        // 构建OkHttpClient
        return OkHttpClient.Builder()
            .connectTimeout(eventSourceConfig.connectTimeout, TimeUnit.MILLISECONDS) // 连接超时设置
            .readTimeout(eventSourceConfig.readTimeout, TimeUnit.MILLISECONDS) // 读取超时设置
            .writeTimeout(eventSourceConfig.writeTimeout, TimeUnit.MILLISECONDS) // 写入超时设置
            .addInterceptor { chain -> // 添加请求拦截器
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", properties.apiKey) // 添加Authorization头
                    .build()
                chain.proceed(request) // 继续处理请求
            }
            .addInterceptor(loggingInterceptor) // 添加日志拦截器
            .build() // 构建OkHttpClient
    }

    /**
     * 配置Retrofit
     * @param okHttpClient OkHttp客户端
     * @param objectMapper ObjectMapper实例
     * @return 返回配置好的Retrofit实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun retrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(properties.baseUrl) // 设置基础URL
            .client(okHttpClient) // 设置OkHttpClient
            .addConverterFactory(JacksonConverterFactory.create(objectMapper)) // 添加Jackson转换工厂
            .build() // 构建Retrofit
    }

    /**
     * 配置UserApi
     * @param retrofit Retrofit实例
     * @return 返回配置好的UserApi实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun userApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java) // 创建UserApi实例
    }

    /**
     * 配置MessagingApi
     * @param retrofit Retrofit实例
     * @return 返回配置好的MessagingApi实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun messagingApi(retrofit: Retrofit): MessagingApi {
        return retrofit.create(MessagingApi::class.java) // 创建MessagingApi实例
    }

    /**
     * 配置HealthApi
     * @param retrofit Retrofit实例
     * @return 返回配置好的HealthApi实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun healthApi(retrofit: Retrofit): HealthApi {
        return retrofit.create(HealthApi::class.java) // 创建HealthApi实例
    }

    /**
     * 配置默认的UserIdentityService
     * @return 返回默认的UserIdentityService实例
     */
    @Bean
    @ConditionalOnMissingBean(UserIdentityService::class)
    fun defaultUserIdentityService(): UserIdentityService {
        return DefaultUserIdentityService() // 返回默认实现
    }

    /**
     * 配置PermissionService
     * @param userApi UserApi实例
     * @return 返回配置好的PermissionService实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun permissionService(userApi: UserApi): PermissionService {
        return LuckPermsPermissionService(userApi) // 返回LuckPermsPermissionService实现
    }

    /**
     * 配置GroupService
     * @param userApi UserApi实例
     * @return 返回配置好的GroupService实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun groupService(userApi: UserApi): GroupService {
        return LuckPermsGroupService(userApi) // 返回LuckPermsGroupService实现
    }

    /**
     * 配置ContextService
     * @param userApi UserApi实例
     * @return 返回配置好的ContextService实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun contextService(userApi: UserApi): ContextService {
        return LuckPermsContextService(userApi) // 返回LuckPermsContextService实现
    }

    /**
     * 配置PermissionAspect
     * @param permissionService 权限服务
     * @param userIdentityService 用户身份服务
     * @return 返回配置好的PermissionAspect实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun permissionAspect(
        permissionService: PermissionService,
        userIdentityService: UserIdentityService
    ): PermissionAspect {
        return PermissionAspect(permissionService, userIdentityService) // 创建PermissionAspect实例
    }

    /**
     * 配置GroupAspect
     * @param groupService 组服务
     * @param userIdentityService 用户身份服务
     * @return 返回配置好的GroupAspect实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun groupAspect(
        groupService: GroupService,
        userIdentityService: UserIdentityService
    ): GroupAspect {
        return GroupAspect(groupService, userIdentityService) // 创建GroupAspect实例
    }

    /**
     * 配置ContextAspect
     * @param contextService 上下文服务
     * @param userIdentityService 用户身份服务
     * @return 返回配置好的ContextAspect实例
     */
    @Bean
    @ConditionalOnMissingBean
    fun contextAspect(
        contextService: ContextService,
        userIdentityService: UserIdentityService
    ): ContextAspect {
        return ContextAspect(contextService, userIdentityService) // 创建ContextAspect实例
    }
}