package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * LuckPerms Starter配置属性
 *
 * @property baseUrl LuckPerms API基础URL
 * @property apiKey LuckPerms API密钥
 * @property enabled 是否启用LuckPerms Bridge（默认：true）
 */
@ConfigurationProperties(prefix = "luck-perms")
data class LuckPermsProperties(
    val baseUrl: String = "http://localhost:8080",
    val apiKey: String = "",
    val enabled: Boolean = true
) 