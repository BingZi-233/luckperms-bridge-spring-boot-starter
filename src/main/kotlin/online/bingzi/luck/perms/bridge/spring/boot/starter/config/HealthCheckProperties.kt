package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "luck-perms.health-check")
data class HealthCheckProperties(
    /**
     * 是否启用健康检查
     */
    val enabled: Boolean = true,
    
    /**
     * 健康检查周期
     */
    val period: Duration = Duration.ofSeconds(30),
    
    /**
     * 健康检查超时时间
     */
    val timeout: Duration = Duration.ofSeconds(5),

    /**
     * 最大重试次数
     */
    val maxAttempts: Int = 3,

    /**
     * 初始重试间隔（毫秒）
     */
    val initialInterval: Long = 2000,

    /**
     * 重试间隔倍数
     */
    val multiplier: Double = 2.0,

    /**
     * 最大重试间隔（毫秒）
     */
    val maxInterval: Long = 10000
) 