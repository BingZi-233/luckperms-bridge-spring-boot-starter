package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import online.bingzi.luck.perms.bridge.spring.boot.starter.manager.EventManager
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * LuckPerms自动配置类
 */
@AutoConfiguration
@EnableConfigurationProperties(LuckPermsConfig::class)
class LuckPermsAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    fun eventManager(luckPermsConfig: LuckPermsConfig): EventManager {
        return EventManager(luckPermsConfig.retrofit(), luckPermsConfig.okHttpClient())
    }
} 