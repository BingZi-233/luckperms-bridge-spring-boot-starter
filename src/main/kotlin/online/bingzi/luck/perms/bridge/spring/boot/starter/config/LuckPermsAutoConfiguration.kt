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
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.ContextService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.GroupService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.PermissionService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.DefaultUserIdentityService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsContextService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsGroupService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.LuckPermsPermissionService
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Primary
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * LuckPerms自动配置类
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(LuckPermsProperties::class)
@ConditionalOnProperty(prefix = "luck-perms", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class LuckPermsAutoConfiguration(private val properties: LuckPermsProperties) {

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
    @ConditionalOnBean(OkHttpClient::class)
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
    @ConditionalOnBean(Retrofit::class)
    fun userApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    /**
     * 配置MessagingApi
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(Retrofit::class)
    fun messagingApi(retrofit: Retrofit): MessagingApi {
        return retrofit.create(MessagingApi::class.java)
    }

    /**
     * 配置HealthApi
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(Retrofit::class)
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
    @ConditionalOnBean(UserApi::class)
    fun permissionService(userApi: UserApi): PermissionService {
        return LuckPermsPermissionService(userApi)
    }

    /**
     * 配置GroupService
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserApi::class)
    fun groupService(userApi: UserApi): GroupService {
        return LuckPermsGroupService(userApi)
    }

    /**
     * 配置ContextService
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(UserApi::class)
    fun contextService(userApi: UserApi): ContextService {
        return LuckPermsContextService(userApi)
    }

    /**
     * 配置PermissionAspect
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean([PermissionService::class, UserIdentityService::class])
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
    @ConditionalOnBean([GroupService::class, UserIdentityService::class])
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
    @ConditionalOnBean([ContextService::class, UserIdentityService::class])
    fun contextAspect(
        contextService: ContextService,
        userIdentityService: UserIdentityService
    ): ContextAspect {
        return ContextAspect(contextService, userIdentityService)
    }
} 