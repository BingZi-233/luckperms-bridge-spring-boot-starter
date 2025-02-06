package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

/**
 * HealthCheckProperties 是一个数据类，用于封装健康检查的配置属性。
 * 该类通过 Spring Boot 的 @ConfigurationProperties 注解自动绑定配置文件中的相关属性。
 * 主要用于配置健康检查的行为，包括启用状态、周期、超时、重试策略等。
 */
@ConfigurationProperties(prefix = "luck-perms.health-check")
data class HealthCheckProperties(
    /**
     * 是否启用健康检查，默认为 true。
     * 当设置为 false 时，健康检查将被禁用。
     */
    val enabled: Boolean = true,
    
    /**
     * 健康检查的周期，默认为 30 秒。
     * 该属性指定健康检查的执行频率。
     */
    val period: Duration = Duration.ofSeconds(30),
    
    /**
     * 健康检查的超时时间，默认为 5 秒。
     * 该属性指定每次健康检查请求的最长等待时间。
     */
    val timeout: Duration = Duration.ofSeconds(5),

    /**
     * 最大重试次数，默认为 3。
     * 当健康检查失败时，系统将会重试的最大次数。
     */
    val maxAttempts: Int = 3,

    /**
     * 初始重试间隔（毫秒），默认为 2000 毫秒。
     * 该属性指定第一次重试之前的等待时间。
     */
    val initialInterval: Long = 2000,

    /**
     * 重试间隔倍数，默认为 2.0。
     * 每次重试之间的间隔会根据该倍数进行递增。
     */
    val multiplier: Double = 2.0,

    /**
     * 最大重试间隔（毫秒），默认为 10000 毫秒。
     * 该属性指定重试之间的最大等待时间，超过该时间则不再增加。
     */
    val maxInterval: Long = 10000
) 