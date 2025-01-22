package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.HealthApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl.HealthCheckService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.retry.annotation.RetryConfiguration
import org.springframework.scheduling.annotation.EnableScheduling
import retrofit2.Retrofit

@Configuration
@EnableScheduling
@EnableConfigurationProperties(HealthCheckProperties::class)
@Import(RetryConfiguration::class)
class HealthCheckConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    fun healthCheckService(healthApi: HealthApi, healthCheckProperties: HealthCheckProperties): HealthCheckService {
        return HealthCheckService(healthApi, healthCheckProperties)
    }
} 