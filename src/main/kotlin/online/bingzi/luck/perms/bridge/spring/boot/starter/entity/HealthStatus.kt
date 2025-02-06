package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import java.time.LocalDateTime

/**
 * HealthStatus 类用于表示服务的健康状态。
 * 该类包含了服务健康的相关信息，如健康状态、最后检查时间、响应时间等。
 * 在整个系统中，它用于监控服务的运行状态，帮助开发者和运维人员及时发现和处理问题。
 */
data class HealthStatus(
    /**
     * 是否健康
     * 表示服务当前的健康状态，true 表示服务健康，false 表示服务不健康。
     */
    var isHealthy: Boolean = true,
    
    /**
     * 最后一次检查时间
     * 记录服务最后一次健康检查的时间，通常用于监控和日志记录。
     */
    var lastCheckTime: LocalDateTime = LocalDateTime.now(),
    
    /**
     * 最后一次响应时间（毫秒）
     * 记录服务最后一次成功响应的时间，以毫秒为单位，通常用于性能监控。
     */
    var lastResponseTime: Long = 0,
    
    /**
     * 连续失败次数
     * 记录服务连续失败的次数，通常用于判断服务的稳定性和可靠性。
     */
    var consecutiveFailures: Int = 0,
    
    /**
     * 最后一次失败时间
     * 记录服务最后一次失败的时间，如果服务没有失败，则为 null。
     */
    var lastFailureTime: LocalDateTime? = null,
    
    /**
     * 服务不可用持续时间（毫秒）
     * 记录服务持续不可用的时间，以毫秒为单位，通常用于评估服务中断的影响。
     */
    var downtime: Long = 0
)