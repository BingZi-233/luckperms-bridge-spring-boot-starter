package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.state.ConnectionState
import java.time.Instant

/**
 * 连接统计信息
 */
data class ConnectionStats(
    val currentState: ConnectionState,
    val retryCount: Int,
    val lastConnectedTime: Instant?,
    val lastDisconnectedTime: Instant?,
    val uptime: Long,
    val downtime: Long
) 