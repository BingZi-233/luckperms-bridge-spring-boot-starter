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

@Configuration
@EnableScheduling
@EnableConfigurationProperties(HealthCheckProperties::class)
@Import(RetryConfiguration::class)
class HealthCheckConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    fun healthCheckManager(): HealthCheckManager {
        return HealthCheckManager()
    }
    
    @Bean
    @ConditionalOnMissingBean
    fun healthCheckRetryListener(healthCheckManager: HealthCheckManager): HealthCheckRetryListener {
        return HealthCheckRetryListener(healthCheckManager)
    }
    
    @Bean
    @ConditionalOnMissingBean
    fun healthCheckService(
        healthApi: HealthApi,
        healthCheckProperties: HealthCheckProperties,
        healthCheckManager: HealthCheckManager,
        healthCheckRetryListener: HealthCheckRetryListener
    ): HealthCheckService {
        return HealthCheckService(
            healthApi,
            healthCheckProperties,
            healthCheckManager,
            healthCheckRetryListener
        )
    }
} 