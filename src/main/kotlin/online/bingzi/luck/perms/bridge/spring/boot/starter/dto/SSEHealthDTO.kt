package online.bingzi.luck.perms.bridge.spring.boot.starter.dto

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType

/**
 * SSE健康状态DTO类
 *
 * 该类用于表示服务器发送事件（SSE）的健康状态信息，包含了与SSE连接相关的各项指标。
 * 主要功能包括存储和传递SSE连接的端点、状态、重试次数、运行时间、停机时间和最后响应时间等信息。
 *
 * 在整个代码结构中，该类充当数据传输对象（DTO），用于在不同层之间传递SSE的健康状态数据。
 *
 * @property endpoint SSE连接的端点地址，类型为String。
 * @property state 当前SSE连接的状态，类型为ConnectionStateType（一个枚举类型）。
 * @property retryCount 当前重试的次数，类型为Int，表示尝试重新建立连接的次数。
 * @property uptime 当前连接的运行时间，单位为毫秒，类型为Long。
 * @property downtime 当前连接的停机时间，单位为毫秒，类型为Long。
 * @property lastResponseTime 上一次响应的时间戳，单位为毫秒，类型为Long。
 */
data class SSEHealthDTO(
    val endpoint: String,            // SSE连接的端点地址
    val state: ConnectionStateType,  // 当前连接的状态
    val retryCount: Int,             // 重试次数
    val uptime: Long,                // 连接运行时间（毫秒）
    val downtime: Long,              // 连接停机时间（毫秒）
    val lastResponseTime: Long       // 最后响应时间（毫秒）
)