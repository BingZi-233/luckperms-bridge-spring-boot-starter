package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.stats

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType

/**
 * 连接统计信息类
 * 此类用于记录和维护SSE（服务器推送事件）连接的各项统计数据，包括连接状态、重试次数、运行时间等信息。
 * 通过该类，可以有效地跟踪连接的健康状态和性能指标。
 *
 * @property currentState 当前连接状态，类型为ConnectionStateType，表示连接的状态（如连接中、已断开等）。
 * @property retryCount 重试次数，类型为Int，表示在连接失败后尝试重新连接的次数。
 * @property uptime 累计运行时间，类型为Long，以毫秒为单位，表示连接成功运行的总时间。
 * @property downtime 累计停机时间，类型为Long，以毫秒为单位，表示连接失败或断开后的总停机时间。
 * @property lastResponseTime 最后一次响应时间，类型为Long，以毫秒为单位，表示上一次成功接收到响应的时间戳。
 */
data class ConnectionStats(
    val currentState: ConnectionStateType, // 当前连接状态
    val retryCount: Int,                    // 重试次数
    val uptime: Long,                       // 累计运行时间（毫秒）
    val downtime: Long,                     // 累计停机时间（毫秒）
    val lastResponseTime: Long              // 最后一次响应时间
) 