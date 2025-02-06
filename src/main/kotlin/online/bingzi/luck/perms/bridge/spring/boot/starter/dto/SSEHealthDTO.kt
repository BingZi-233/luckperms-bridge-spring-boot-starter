package online.bingzi.luck.perms.bridge.spring.boot.starter.dto

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType

/**
 * SSE健康状态DTO
 *
 * @property endpoint SSE端点
 * @property state 连接状态
 * @property retryCount 重试次数
 * @property uptime 运行时间（毫秒）
 * @property downtime 停机时间（毫秒）
 * @property lastResponseTime 最后响应时间
 */
data class SSEHealthDTO(
    val endpoint: String,
    val state: ConnectionStateType,
    val retryCount: Int,
    val uptime: Long,
    val downtime: Long,
    val lastResponseTime: Long
) 