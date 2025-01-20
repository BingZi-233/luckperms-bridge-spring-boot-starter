package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * LuckPerms配置类
 */
@Configuration
@ConfigurationProperties(prefix = "luckperms")
class LuckPermsConfig {
    /**
     * LuckPerms API的基础URL
     */
    var baseUrl: String = "http://localhost:8080"

    /**
     * API密钥
     */
    var apiKey: String? = null

    /**
     * 连接超时时间（秒）
     */
    var connectTimeout: Long = 10

    /**
     * 读取超时时间（秒）
     */
    var readTimeout: Long = 30

    /**
     * 写入超时时间（秒）
     */
    var writeTimeout: Long = 30

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()

    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .apply {
                        apiKey?.let { header("Authorization", "Bearer $it") }
                    }
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    @Bean
    fun retrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }
} 