package online.bingzi.luck.perms.bridge.spring.boot.starter.dto

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType

/**
 * SSE健康状态数据传输对象
 * 用于传输SSE连接的健康状态信息
 *
 * @property endpoint 连接端点
 * @property state 当前连接状态
 * @property retryCount 重试次数
 * @property lastResponseTime 最后响应时间
 */
data class SSEHealthDTO(
    val endpoint: String,
    val state: ConnectionStateType,
    val retryCount: Int,
    val lastResponseTime: Long
)