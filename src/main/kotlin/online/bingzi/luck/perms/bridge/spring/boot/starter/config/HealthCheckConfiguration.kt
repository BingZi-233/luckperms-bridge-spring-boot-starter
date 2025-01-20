package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.HealthApi
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import retrofit2.Retrofit

@Configuration
@EnableScheduling
@EnableConfigurationProperties(HealthCheckProperties::class)
class HealthCheckConfiguration {
    
    @Bean
    fun healthApi(retrofit: Retrofit): HealthApi {
        return retrofit.create(HealthApi::class.java)
    }
} 