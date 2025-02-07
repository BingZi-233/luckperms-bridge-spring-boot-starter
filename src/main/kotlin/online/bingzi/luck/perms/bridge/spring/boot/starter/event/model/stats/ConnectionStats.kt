package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.stats

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType

/**
 * 连接统计信息数据类
 * 用于存储SSE连接的统计信息
 *
 * @property currentState 当前连接状态
 * @property retryCount 重试次数
 * @property lastResponseTime 最后响应时间
 */
data class ConnectionStats(
    val currentState: ConnectionStateType,
    val retryCount: Int,
    val lastResponseTime: Long
) 