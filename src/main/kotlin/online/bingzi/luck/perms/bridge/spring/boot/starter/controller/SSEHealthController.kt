package online.bingzi.luck.perms.bridge.spring.boot.starter.controller

import online.bingzi.luck.perms.bridge.spring.boot.starter.dto.SSEHealthDTO
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSEConnectionManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * SSE健康检查控制器
 * 提供SSE连接状态的查询接口
 */
@RestController
@RequestMapping("/api/v1/sse/health")
class SSEHealthController(
    private val connectionManager: SSEConnectionManager
) {

    /**
     * 获取所有SSE连接的健康状态
     *
     * @return 所有SSE连接的健康状态列表
     */
    @GetMapping
    fun getAllConnectionHealth(): List<SSEHealthDTO> {
        return connectionManager.getAllConnectionStats().map { (endpoint, stats) ->
            SSEHealthDTO(
                endpoint = endpoint,
                state = stats.currentState,
                retryCount = stats.retryCount,
                uptime = stats.uptime,
                downtime = stats.downtime,
                lastResponseTime = stats.lastResponseTime
            )
        }
    }

    /**
     * 获取SSE连接的健康状态摘要
     *
     * @return 健康状态摘要信息
     */
    @GetMapping("/summary")
    fun getConnectionHealthSummary(): Map<String, Any> {
        val stats = connectionManager.getAllConnectionStats()
        val totalConnections = stats.size
        val activeConnections = stats.count { it.value.currentState.isConnected() }
        val totalRetries = stats.values.sumOf { it.retryCount }
        val totalUptime = stats.values.maxOfOrNull { it.uptime } ?: 0
        val totalDowntime = stats.values.sumOf { it.downtime }
        
        return mapOf(
            "totalConnections" to totalConnections,
            "activeConnections" to activeConnections,
            "totalRetries" to totalRetries,
            "totalUptime" to totalUptime,
            "totalDowntime" to totalDowntime,
            "healthScore" to calculateHealthScore(activeConnections, totalConnections, totalRetries)
        )
    }

    /**
     * 计算健康分数
     * 分数范围：0-100
     * 计算公式：(活跃连接数 / 总连接数) * 100 - min(重试次数影响, 20)
     * 重试次数影响：每次重试扣1分，最多扣20分
     */
    private fun calculateHealthScore(activeConnections: Int, totalConnections: Int, totalRetries: Int): Int {
        if (totalConnections == 0) return 100
        val baseScore = (activeConnections.toDouble() / totalConnections.toDouble() * 100).toInt()
        val retryPenalty = minOf(totalRetries, 20)
        return maxOf(baseScore - retryPenalty, 0)
    }
} 