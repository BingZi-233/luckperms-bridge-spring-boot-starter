package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.stats

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType

/**
 * 连接统计信息
 * 用于记录SSE连接的各项统计数据
 *
 * @property currentState 当前连接状态
 * @property retryCount 重试次数
 * @property uptime 累计运行时间（毫秒）
 * @property downtime 累计停机时间（毫秒）
 * @property lastResponseTime 最后一次响应时间
 */
data class ConnectionStats(
    val currentState: ConnectionStateType,
    val retryCount: Int,
    val uptime: Long,
    val downtime: Long,
    val lastResponseTime: Long
) 