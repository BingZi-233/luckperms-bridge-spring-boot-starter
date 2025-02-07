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
    // 日志记录器，用于记录SSE连接的状态
    private val log = LoggerFactory.getLogger(SSEConnectionManager::class.java)
    // 存储所有连接信息的线程安全哈希表
    private val connections = ConcurrentHashMap<String, ConnectionInfo>()

    /**
     * 获取或创建连接信息
     * 如果指定的端点不存在连接信息，则创建一个新的连接信息并返回
     * 
     * @param endpoint 连接的端点，类型为String
     * @return 连接信息
     */
    private fun getOrCreateConnectionInfo(endpoint: String): ConnectionInfo {
        return connections.computeIfAbsent(endpoint) { ConnectionInfo() }
    }

    /**
     * 更新连接状态
     * 根据给定的端点和新的状态更新连接信息中的状态
     * 
     * @param endpoint 连接的端点，类型为String
     * @param newState 新的连接状态，类型为ConnectionStateType
     */
    fun updateState(endpoint: String, newState: ConnectionStateType) {
        val connectionInfo = getOrCreateConnectionInfo(endpoint)
        connectionInfo.updateState(newState, endpoint)
        
        // 如果连接成功，重置重试计数
        if (newState == ConnectionStateType.CONNECTED) {
            connectionInfo.resetRetryCount()
        }
    }

    /**
     * 获取当前状态
     * 返回指定端点的当前连接状态
     * 
     * @param endpoint 连接的端点，类型为String
     * @return 当前连接状态，类型为ConnectionStateType
     */
    fun getCurrentState(endpoint: String): ConnectionStateType = 
        getOrCreateConnectionInfo(endpoint).currentState.get()

    /**
     * 获取重试次数
     * 返回指定端点的重试次数
     * 
     * @param endpoint 连接的端点，类型为String
     * @return 重试次数，类型为Int
     */
    fun getRetryCount(endpoint: String): Int = 
        getOrCreateConnectionInfo(endpoint).retryCount.get()

    /**
     * 获取连接统计信息
     * 返回指定端点的连接统计信息
     * 
     * @param endpoint 连接的端点，类型为String
     * @return 连接统计信息，类型为ConnectionStats
     */
    fun getConnectionStats(endpoint: String): ConnectionStats = 
        getOrCreateConnectionInfo(endpoint).getStats()

    /**
     * 获取所有连接的统计信息
     * 返回所有端点的连接统计信息
     * 
     * @return 包含所有连接统计信息的Map，键为端点，值为统计信息
     */
    fun getAllConnectionStats(): Map<String, ConnectionStats> =
        connections.mapValues { it.value.getStats() }

    /**
     * 移除连接信息
     * 根据指定的端点移除连接信息
     * 
     * @param endpoint 连接的端点，类型为String
     */
    fun removeConnection(endpoint: String) {
        connections.remove(endpoint)
        log.info("已移除SSE连接信息 - 订阅端点: {}", endpoint)
    }

    /**
     * 单个连接的信息封装
     * 用于保存单个SSE连接的状态和相关统计数据
     */
    private inner class ConnectionInfo {
        // 当前连接状态，使用AtomicReference以支持线程安全的更新
        val currentState = AtomicReference(ConnectionStateType.DISCONNECTED)
        // 记录重试次数，使用AtomicInteger以支持线程安全的更新
        val retryCount = AtomicInteger(0)
        // 最后成功连接的时间，使用AtomicLong以支持线程安全的更新
        val lastSuccessTime = AtomicLong(0)
        // 最后失败连接的时间，使用AtomicLong以支持线程安全的更新
        val lastFailureTime = AtomicLong(0)
        // 最后响应的时间，使用AtomicLong以支持线程安全的更新
        val lastResponseTime = AtomicLong(0)
        // 最后状态变化时间，使用AtomicLong以支持线程安全的更新
        val lastStateChangeTime = AtomicLong(System.currentTimeMillis())
        // 上一次断开连接的时间，使用AtomicLong以支持线程安全的更新
        val lastDisconnectTime = AtomicLong(0)

        /**
         * 更新连接状态
         * 根据新的状态更新当前状态，并记录相关的时间信息
         * 
         * @param newState 新的连接状态，类型为ConnectionStateType
         * @param endpoint 连接的端点，类型为String
         */
        fun updateState(newState: ConnectionStateType, endpoint: String) {
            val oldState = currentState.get()
            // 尝试更新状态，如果成功则执行后续逻辑
            if (currentState.compareAndSet(oldState, newState)) {
                val currentTime = System.currentTimeMillis()
                
                // 更新最后状态变化时间为当前时间
                lastStateChangeTime.set(currentTime)
                
                when (newState) {
                    ConnectionStateType.CONNECTED -> {
                        lastSuccessTime.set(currentTime)
                        lastResponseTime.set(currentTime)
                        if (oldState != ConnectionStateType.CONNECTED) {
                            log.info("SSE连接已建立 - 订阅端点: {}, 重试次数: {}", 
                                endpoint, retryCount.get())
                        }
                    }
                    ConnectionStateType.DISCONNECTED -> {
                        lastFailureTime.set(currentTime)
                        if (oldState != ConnectionStateType.DISCONNECTED) {
                            log.warn("SSE连接已断开 - 订阅端点: {}, 重试次数: {}", 
                                endpoint, retryCount.get())
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
                        log.info("SSE连接已暂停 - 订阅端点: {}", endpoint)
                    }
                    ConnectionStateType.FAILED -> {
                        lastFailureTime.set(currentTime)
                        log.error("SSE连接已失败 - 订阅端点: {}, 重试次数: {}", 
                            endpoint, retryCount.get())
                    }
                    ConnectionStateType.CLOSED -> {
                        lastFailureTime.set(currentTime)
                        log.info("SSE连接已关闭 - 订阅端点: {}", endpoint)
                    }
                    ConnectionStateType.UNKNOWN -> {
                        log.warn("SSE连接状态未知 - 订阅端点: {}, 重试次数: {}", endpoint, retryCount.get())
                    }
                }
            }
        }

        /**
         * 重置重试计数
         */
        fun resetRetryCount() {
            retryCount.set(0)
        }

        /**
         * 获取连接的统计信息
         * 返回当前连接的状态和统计数据
         * 
         * @return 连接统计信息，类型为ConnectionStats
         */
        fun getStats(): ConnectionStats = ConnectionStats(
            currentState = currentState.get(),
            retryCount = retryCount.get(),
            lastResponseTime = lastResponseTime.get(),
            lastSuccessTime = lastSuccessTime.get(),
            lastFailureTime = lastFailureTime.get()
        )
    }
}