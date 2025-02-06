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
 * 
 * 该类用于配置LuckPerms的相关属性，包括API的基础URL、连接超时时间、读取和写入超时时间等。
 * 通过Spring的@Configuration和@ConfigurationProperties注解，能够将配置类与配置文件中的属性绑定。
 */
@Configuration
@ConfigurationProperties(prefix = "luckperms")
class LuckPermsConfig {
    
    /**
     * LuckPerms API的基础URL
     * 
     * 该属性定义了LuckPerms API的基础地址，默认为"http://localhost:8080"。
     */
    var baseUrl: String = "http://localhost:8080"

    /**
     * API密钥
     * 
     * 该属性用于存储API的密钥，以便进行身份验证。默认为null，表示未设置密钥。
     */
    var apiKey: String? = null

    /**
     * 连接超时时间（秒）
     * 
     * 该属性定义了与LuckPerms API建立连接时的超时时间，默认为10秒。
     */
    var connectTimeout: Long = 10

    /**
     * 读取超时时间（秒）
     * 
     * 该属性定义了从LuckPerms API读取响应时的超时时间，默认为30秒。
     */
    var readTimeout: Long = 30

    /**
     * 写入超时时间（秒）
     * 
     * 该属性定义了向LuckPerms API写入数据时的超时时间，默认为30秒。
     */
    var writeTimeout: Long = 30

    /**
     * 创建ObjectMapper的Bean
     * 
     * @return ObjectMapper 实例，用于处理JSON数据的序列化和反序列化。
     */
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()

    /**
     * 创建OkHttpClient的Bean
     * 
     * @return OkHttpClient 实例，配置了超时时间和拦截器。
     * 
     * 该方法通过构建OkHttpClient，并设置连接、读取和写入超时时间，同时配置了请求拦截器以添加API密钥（如果存在）。
     * 还添加了一个日志拦截器以进行基本的HTTP请求日志记录。
     */
    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)  // 设置连接超时时间
            .readTimeout(readTimeout, TimeUnit.SECONDS)        // 设置读取超时时间
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)      // 设置写入超时时间
            .addInterceptor { chain ->  // 添加请求拦截器
                val request = chain.request().newBuilder()
                    .apply {
                        apiKey?.let { header("Authorization", "Bearer $it") }  // 如果API密钥存在，添加到请求头中
                    }
                    .build()
                chain.proceed(request)  // 执行请求
            }
            .addInterceptor(HttpLoggingInterceptor().apply {  // 添加日志拦截器
                level = HttpLoggingInterceptor.Level.BASIC  // 设置日志级别为基本
            })
            .build()  // 构建OkHttpClient实例
    }

    /**
     * 创建Retrofit的Bean
     * 
     * @param okHttpClient OkHttpClient 实例，用于网络请求的配置。
     * @param objectMapper ObjectMapper 实例，用于JSON转换。
     * @return Retrofit 实例，用于进行网络请求。
     * 
     * 该方法通过Retrofit.Builder构建Retrofit实例，配置基础URL、OkHttpClient以及JSON转换工厂。
     */
    @Bean
    fun retrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)  // 设置基础URL
            .client(okHttpClient)  // 设置OkHttpClient
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))  // 设置Jackson转换工厂
            .build()  // 构建Retrofit实例
    }
} 