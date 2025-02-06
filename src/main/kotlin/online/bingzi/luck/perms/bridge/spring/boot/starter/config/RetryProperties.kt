package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 重试配置属性类
 * 此类用于定义和管理与重试机制相关的配置属性。
 * 主要功能是通过Spring Boot的配置属性功能，来读取和存储
 * 应用程序中的重试策略的相关参数。
 */
@ConfigurationProperties(prefix = "luck-perms.retry")
data class RetryProperties(
    /**
     * 初始重试间隔（毫秒）
     * 定义第一次重试的等待时间，单位为毫秒。
     * 默认值为1000毫秒（1秒）。
     */
    var initialInterval: Long = 1000,

    /**
     * 重试间隔倍数
     * 定义每次重试之间的等待时间将会以此倍数增加。
     * 默认值为2.0，表示每次重试的间隔时间将是上一次的两倍。
     */
    var multiplier: Double = 2.0,

    /**
     * 最大重试间隔（毫秒）
     * 定义重试间隔的上限，单位为毫秒。
     * 一旦重试间隔达到此值，将不再增加。
     * 默认值为30000毫秒（30秒）。
     */
    var maxInterval: Long = 30000,

    /**
     * 最大重试次数
     * 定义在操作失败后最多可以进行多少次重试。
     * 默认值为5，表示最多重试5次。
     */
    var maxAttempts: Int = 5
) 