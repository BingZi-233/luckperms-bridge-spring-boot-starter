package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType
import java.time.Instant

/**
 * 连接统计信息
 */
data class ConnectionStats(
    val currentState: ConnectionStateType,
    val retryCount: Int,
    val lastConnectedTime: Instant?,
    val lastDisconnectedTime: Instant?,
    val uptime: Long,
    val downtime: Long
) 