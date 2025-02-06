package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType
import java.time.Instant

/**
 * 连接统计信息类
 * 
 * 该类用于表示连接的统计信息，包括当前状态、重试次数、最后连接时间、最后断开时间、正常运行时间和停机时间等信息。
 * 主要用于在系统中跟踪和管理连接的状态。
 */
data class ConnectionStats(
    /**
     * 当前连接状态
     * 表示连接的当前状态，类型为 ConnectionStateType，可能的值包括连接中、已连接、断开等状态。
     */
    val currentState: ConnectionStateType,
    
    /**
     * 重试次数
     * 表示连接尝试重试的次数，类型为 Int，通常为非负整数。
     */
    val retryCount: Int,
    
    /**
     * 最后连接时间
     * 表示最后一次成功连接的时间，类型为 Instant，可以为 null 表示尚未连接过。
     */
    val lastConnectedTime: Instant?,
    
    /**
     * 最后断开时间
     * 表示最后一次断开连接的时间，类型为 Instant，可以为 null 表示尚未断开过。
     */
    val lastDisconnectedTime: Instant?,
    
    /**
     * 正常运行时间
     * 表示连接正常运行的总时间，单位为毫秒，类型为 Long，通常为非负整数。
     */
    val uptime: Long,
    
    /**
     * 停机时间
     * 表示连接断开或停机的总时间，单位为毫秒，类型为 Long，通常为非负整数。
     */
    val downtime: Long
) 