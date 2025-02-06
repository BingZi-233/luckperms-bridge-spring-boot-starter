package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * LuckPerms Starter配置属性类
 *
 * 该类用于封装LuckPerms相关的配置属性，通过Spring Boot的配置文件
 * （如application.yml或application.properties）进行读取和管理。
 * 主要功能是提供LuckPerms API的基础URL、API密钥及启用状态。
 *
 * @property baseUrl LuckPerms API的基础URL，默认值为"http://localhost:8080"
 * @property apiKey LuckPerms API的密钥，用于身份验证，默认值为空字符串
 * @property enabled 指示是否启用LuckPerms Bridge，默认值为true
 */
@ConfigurationProperties(prefix = "luck-perms")
data class LuckPermsProperties(
    /** 
     * LuckPerms API的基础URL
     * 该属性用于指定与LuckPerms API交互时的基础URL，
     * 通常设置为LuckPerms服务器的地址。
     * 默认值为"http://localhost:8080"。
     */
    val baseUrl: String = "http://localhost:8080",

    /** 
     * LuckPerms API的密钥
     * 该属性用于提供与LuckPerms API交互时所需的身份验证密钥。
     * 开发者需要在LuckPerms控制台中生成并配置此密钥。
     * 默认值为空字符串，表示未配置密钥。
     */
    val apiKey: String = "",

    /** 
     * 是否启用LuckPerms Bridge
     * 该属性用于指示LuckPerms Bridge的启用状态。
     * 默认值为true，表示启用。
     * 如果设置为false，将不会加载LuckPerms相关的功能。
     */
    val enabled: Boolean = true
)