package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.HealthApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckManager
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckRetryListener
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.HealthCheckService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.retry.annotation.RetryConfiguration
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * 配置类，负责配置健康检查相关的Bean。
 * 该类主要用于创建健康检查管理器、健康检查重试监听器和健康检查服务的实例。
 */
@Configuration
@EnableScheduling // 启用定时任务调度
@EnableConfigurationProperties(HealthCheckProperties::class) // 启用健康检查属性配置类
@Import(RetryConfiguration::class) // 导入重试配置类
class HealthCheckConfiguration {

    /**
     * 创建健康检查管理器的Bean。
     * 
     * @return HealthCheckManager 健康检查管理器实例
     */
    @Bean
    @ConditionalOnMissingBean // 如果没有已有的HealthCheckManager Bean，则创建一个新的
    fun healthCheckManager(): HealthCheckManager {
        return HealthCheckManager() // 返回新的健康检查管理器实例
    }

    /**
     * 创建健康检查重试监听器的Bean。
     * 
     * @param healthCheckManager 健康检查管理器实例，用于执行健康检查
     * @return HealthCheckRetryListener 健康检查重试监听器实例
     */
    @Bean
    @ConditionalOnMissingBean // 如果没有已有的HealthCheckRetryListener Bean，则创建一个新的
    fun healthCheckRetryListener(healthCheckManager: HealthCheckManager): HealthCheckRetryListener {
        return HealthCheckRetryListener(healthCheckManager) // 返回新的健康检查重试监听器实例
    }

    /**
     * 创建健康检查服务的Bean。
     * 
     * @param healthApi 健康检查API，提供健康检查的相关接口
     * @param healthCheckProperties 健康检查属性配置，包含健康检查的相关配置参数
     * @param healthCheckManager 健康检查管理器实例，用于执行健康检查
     * @param healthCheckRetryListener 健康检查重试监听器实例，用于处理重试逻辑
     * @return HealthCheckService 健康检查服务实例
     */
    @Bean
    @ConditionalOnMissingBean // 如果没有已有的HealthCheckService Bean，则创建一个新的
    fun healthCheckService(
        healthApi: HealthApi,
        healthCheckProperties: HealthCheckProperties,
        healthCheckManager: HealthCheckManager,
        healthCheckRetryListener: HealthCheckRetryListener
    ): HealthCheckService {
        return HealthCheckService(
            healthApi, // 注入健康检查API
            healthCheckProperties, // 注入健康检查属性配置
            healthCheckManager, // 注入健康检查管理器
            healthCheckRetryListener // 注入健康检查重试监听器
        ) // 返回新的健康检查服务实例
    }
}