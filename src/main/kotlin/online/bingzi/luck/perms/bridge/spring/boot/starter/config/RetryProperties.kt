package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 重试配置属性
 * 用于配置Spring-Retry的重试策略参数
 */
@ConfigurationProperties(prefix = "luck-perms.retry")
data class RetryProperties(
    /**
     * 初始重试间隔（毫秒）
     */
    var initialInterval: Long = 1000,

    /**
     * 重试间隔倍数
     */
    var multiplier: Double = 2.0,

    /**
     * 最大重试间隔（毫秒）
     */
    var maxInterval: Long = 30000,

    /**
     * 最大重试次数
     */
    var maxAttempts: Int = 5
) 