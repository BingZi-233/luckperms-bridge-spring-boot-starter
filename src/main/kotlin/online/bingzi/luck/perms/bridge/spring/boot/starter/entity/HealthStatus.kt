package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import java.time.LocalDateTime

data class HealthStatus(
    /**
     * 是否健康
     */
    var isHealthy: Boolean = true,
    
    /**
     * 最后一次检查时间
     */
    var lastCheckTime: LocalDateTime = LocalDateTime.now(),
    
    /**
     * 最后一次响应时间（毫秒）
     */
    var lastResponseTime: Long = 0,
    
    /**
     * 连续失败次数
     */
    var consecutiveFailures: Int = 0,
    
    /**
     * 最后一次失败时间
     */
    var lastFailureTime: LocalDateTime? = null,
    
    /**
     * 服务不可用持续时间（毫秒）
     */
    var downtime: Long = 0
) 