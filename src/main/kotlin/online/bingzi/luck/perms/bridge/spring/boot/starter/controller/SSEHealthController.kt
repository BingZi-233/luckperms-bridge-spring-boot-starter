package online.bingzi.luck.perms.bridge.spring.boot.starter.controller

import online.bingzi.luck.perms.bridge.spring.boot.starter.dto.SSEHealthDTO
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse.SSEConnectionManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * SSE健康检查控制器
 * 该控制器提供SSE（Server-Sent Events）连接状态的查询接口，允许客户端获取SSE连接的健康状态信息。
 */
@RestController
@RequestMapping("/api/v1/sse/health") // 定义请求路径为/api/v1/sse/health
class SSEHealthController(
    private val connectionManager: SSEConnectionManager // 用于管理SSE连接状态的连接管理器
) {

    /**
     * 获取所有SSE连接的健康状态
     *
     * @return 所有SSE连接的健康状态列表，列表中的每个元素包含连接的详细健康信息
     */
    @GetMapping // 处理GET请求
    fun getAllConnectionHealth(): List<SSEHealthDTO> {
        // 从连接管理器获取所有连接的状态统计信息，并将其转换为SSEHealthDTO对象
        return connectionManager.getAllConnectionStats().map { (endpoint, stats) ->
            SSEHealthDTO(
                endpoint = endpoint, // 连接的端点
                state = stats.currentState, // 当前连接状态
                retryCount = stats.retryCount, // 重试次数
                uptime = stats.uptime, // 正常运行时间
                downtime = stats.downtime, // 停机时间
                lastResponseTime = stats.lastResponseTime // 最后响应时间
            )
        }
    }

    /**
     * 获取SSE连接的健康状态摘要
     *
     * @return 健康状态摘要信息，包括总连接数、活跃连接数、总重试次数、总正常运行时间、总停机时间等
     */
    @GetMapping("/summary") // 处理GET请求，路径为/api/v1/sse/health/summary
    fun getConnectionHealthSummary(): Map<String, Any> {
        // 获取所有连接的状态统计信息
        val stats = connectionManager.getAllConnectionStats()
        val totalConnections = stats.size // 总连接数
        // 计算当前活跃连接数
        val activeConnections = stats.count { it.value.currentState.isConnected() }
        val totalRetries = stats.values.sumOf { it.retryCount } // 计算总重试次数
        val totalUptime = stats.values.maxOfOrNull { it.uptime } ?: 0 // 计算总正常运行时间
        val totalDowntime = stats.values.sumOf { it.downtime } // 计算总停机时间
        
        // 返回健康状态摘要信息的映射
        return mapOf(
            "totalConnections" to totalConnections, // 总连接数
            "activeConnections" to activeConnections, // 活跃连接数
            "totalRetries" to totalRetries, // 总重试次数
            "totalUptime" to totalUptime, // 总正常运行时间
            "totalDowntime" to totalDowntime, // 总停机时间
            "healthScore" to calculateHealthScore(activeConnections, totalConnections, totalRetries) // 健康分数
        )
    }

    /**
     * 计算健康分数
     * 分数范围：0-100
     * 计算公式：(活跃连接数 / 总连接数) * 100 - min(重试次数影响, 20)
     * 重试次数影响：每次重试扣1分，最多扣20分
     *
     * @param activeConnections 活跃连接数，类型为Int
     * @param totalConnections 总连接数，类型为Int
     * @param totalRetries 总重试次数，类型为Int
     * @return 计算得到的健康分数，类型为Int
     */
    private fun calculateHealthScore(activeConnections: Int, totalConnections: Int, totalRetries: Int): Int {
        // 如果没有连接，则返回满分100
        if (totalConnections == 0) return 100
        // 计算基础分数
        val baseScore = (activeConnections.toDouble() / totalConnections.toDouble() * 100).toInt()
        // 计算重试惩罚，最多扣20分
        val retryPenalty = minOf(totalRetries, 20)
        // 返回计算后的健康分数，确保不低于0
        return maxOf(baseScore - retryPenalty, 0)
    }
}