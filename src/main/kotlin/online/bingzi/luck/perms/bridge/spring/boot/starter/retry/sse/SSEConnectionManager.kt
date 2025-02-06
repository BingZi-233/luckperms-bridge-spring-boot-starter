package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.stats.ConnectionStats
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicInteger

/**
 * SSE连接管理器
 * 负责管理多个SSE连接的状态和统计信息
 */
@Component
class SSEConnectionManager {
    private val log = LoggerFactory.getLogger(SSEConnectionManager::class.java)
    private val connections = ConcurrentHashMap<String, ConnectionInfo>()

    /**
     * 获取或创建连接信息
     */
    private fun getOrCreateConnectionInfo(endpoint: String): ConnectionInfo {
        return connections.computeIfAbsent(endpoint) { ConnectionInfo() }
    }

    /**
     * 更新连接状态
     */
    fun updateState(endpoint: String, newState: ConnectionStateType) {
        val connectionInfo = getOrCreateConnectionInfo(endpoint)
        connectionInfo.updateState(newState, endpoint)
    }

    /**
     * 获取当前状态
     */
    fun getCurrentState(endpoint: String): ConnectionStateType = 
        getOrCreateConnectionInfo(endpoint).currentState.get()

    /**
     * 获取重试次数
     */
    fun getRetryCount(endpoint: String): Int = 
        getOrCreateConnectionInfo(endpoint).retryCount.get()

    /**
     * 获取连接统计信息
     */
    fun getConnectionStats(endpoint: String): ConnectionStats = 
        getOrCreateConnectionInfo(endpoint).getStats()

    /**
     * 获取所有连接的统计信息
     */
    fun getAllConnectionStats(): Map<String, ConnectionStats> =
        connections.mapValues { it.value.getStats() }

    /**
     * 移除连接信息
     */
    fun removeConnection(endpoint: String) {
        connections.remove(endpoint)
        log.info("已移除SSE连接信息 - 订阅端点: {}", endpoint)
    }

    /**
     * 单个连接的信息封装
     */
    private inner class ConnectionInfo {
        val currentState = AtomicReference(ConnectionStateType.DISCONNECTED)
        val retryCount = AtomicInteger(0)
        val lastSuccessTime = AtomicLong(0)
        val lastFailureTime = AtomicLong(0)
        val lastResponseTime = AtomicLong(0)
        val startTime = AtomicLong(System.currentTimeMillis())
        val totalDowntime = AtomicLong(0)
        val lastStateChangeTime = AtomicLong(System.currentTimeMillis())

        fun updateState(newState: ConnectionStateType, endpoint: String) {
            val oldState = currentState.get()
            if (currentState.compareAndSet(oldState, newState)) {
                val currentTime = System.currentTimeMillis()
                val stateChangeDuration = currentTime - lastStateChangeTime.get()
                
                // 更新累计停机时间
                if (oldState in listOf(ConnectionStateType.DISCONNECTED, ConnectionStateType.FAILED, ConnectionStateType.SUSPENDED)) {
                    totalDowntime.addAndGet(stateChangeDuration)
                }
                
                lastStateChangeTime.set(currentTime)
                
                when (newState) {
                    ConnectionStateType.CONNECTED -> {
                        lastSuccessTime.set(currentTime)
                        lastResponseTime.set(currentTime)
                        if (oldState != ConnectionStateType.CONNECTED) {
                            log.info("SSE连接已建立 - 订阅端点: {}, 重试次数: {}, 累计运行时间: {}ms, 累计停机时间: {}ms", 
                                endpoint, retryCount.get(), calculateUptime(), calculateDowntime())
                        }
                    }
                    ConnectionStateType.DISCONNECTED -> {
                        lastFailureTime.set(currentTime)
                        if (oldState != ConnectionStateType.DISCONNECTED) {
                            log.warn("SSE连接已断开 - 订阅端点: {}, 重试次数: {}, 累计停机时间: {}ms", 
                                endpoint, retryCount.get(), calculateDowntime())
                        }
                    }
                    ConnectionStateType.CONNECTING -> {
                        log.info("正在建立SSE连接 - 订阅端点: {}", endpoint)
                    }
                    ConnectionStateType.RETRYING -> {
                        retryCount.incrementAndGet()
                        log.info("正在重试SSE连接 - 订阅端点: {}, 第{}次尝试", endpoint, retryCount.get())
                    }
                    ConnectionStateType.SUSPENDED -> {
                        lastFailureTime.set(currentTime)
                        log.info("SSE连接已暂停 - 订阅端点: {}, 累计运行时间: {}ms, 累计停机时间: {}ms", 
                            endpoint, calculateUptime(), calculateDowntime())
                    }
                    ConnectionStateType.FAILED -> {
                        lastFailureTime.set(currentTime)
                        log.error("SSE连接已失败 - 订阅端点: {}, 重试次数: {}, 累计停机时间: {}ms", 
                            endpoint, retryCount.get(), calculateDowntime())
                    }
                    ConnectionStateType.CLOSED -> {
                        lastFailureTime.set(currentTime)
                        log.info("SSE连接已关闭 - 订阅端点: {}, 累计运行时间: {}ms, 累计停机时间: {}ms", 
                            endpoint, calculateUptime(), calculateDowntime())
                    }
                    ConnectionStateType.UNKNOWN -> {
                        log.warn("SSE连接状态未知 - 订阅端点: {}, 重试次数: {}", endpoint, retryCount.get())
                    }
                }
            }
        }

        fun calculateUptime(): Long {
            val totalTime = System.currentTimeMillis() - startTime.get()
            return totalTime - calculateDowntime()
        }

        fun calculateDowntime(): Long {
            var downtime = totalDowntime.get()
            
            // 如果当前状态是断开状态，加上当前的断开时长
            if (currentState.get() in listOf(ConnectionStateType.DISCONNECTED, ConnectionStateType.FAILED, ConnectionStateType.SUSPENDED)) {
                downtime += System.currentTimeMillis() - lastStateChangeTime.get()
            }
            
            return downtime
        }

        fun getStats(): ConnectionStats = ConnectionStats(
            currentState = currentState.get(),
            retryCount = retryCount.get(),
            uptime = calculateUptime(),
            downtime = calculateDowntime(),
            lastResponseTime = lastResponseTime.get()
        )
    }
}
