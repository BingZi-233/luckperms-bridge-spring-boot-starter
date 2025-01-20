package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.HealthApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.MessagingApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * LuckPerms自动配置类
 */
@Configuration
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
} 