package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.ConnectionStats
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.state.ConnectionState
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * SSE连接状态管理器
 * 负责管理和监控SSE连接的状态
 */
@Component
class SSEConnectionManager {
    private val log = LoggerFactory.getLogger(SSEConnectionManager::class.java)
    
    // 当前连接状态
    private val currentState = AtomicReference(ConnectionState.DISCONNECTED)
    
    // 重试计数器
    private val retryCount = AtomicInteger(0)
    
    // 最后一次连接时间
    private val lastConnectedTime = AtomicReference<Instant>()
    
    // 最后一次断开时间
    private val lastDisconnectedTime = AtomicReference<Instant>()
    
    /**
     * 更新连接状态
     */
    fun updateState(newState: ConnectionState) {
        val oldState = currentState.get()
        if (currentState.compareAndSet(oldState, newState)) {
            when (newState) {
                ConnectionState.CONNECTED -> {
                    lastConnectedTime.set(Instant.now())
                    retryCount.set(0)
                    log.info("SSE连接已建立")
                }
                ConnectionState.DISCONNECTED -> {
                    lastDisconnectedTime.set(Instant.now())
                    log.warn("SSE连接已断开")
                }
                ConnectionState.CONNECTING -> {
                    retryCount.incrementAndGet()
                    log.info("正在尝试重新连接 SSE，重试次数: ${retryCount.get()}")
                }
            }
        }
    }
    
    /**
     * 获取当前连接状态
     */
    fun getCurrentState(): ConnectionState = currentState.get()
    
    /**
     * 获取当前重试次数
     */
    fun getRetryCount(): Int = retryCount.get()
    
    /**
     * 获取连接统计信息
     */
    fun getConnectionStats(): ConnectionStats {
        return ConnectionStats(
            currentState = currentState.get(),
            retryCount = retryCount.get(),
            lastConnectedTime = lastConnectedTime.get(),
            lastDisconnectedTime = lastDisconnectedTime.get(),
            uptime = calculateUptime(),
            downtime = calculateDowntime()
        )
    }
    
    /**
     * 计算正常运行时间（毫秒）
     */
    private fun calculateUptime(): Long {
        val lastConnected = lastConnectedTime.get() ?: return 0
        return if (currentState.get() == ConnectionState.CONNECTED) {
            Instant.now().toEpochMilli() - lastConnected.toEpochMilli()
        } else {
            val lastDisconnected = lastDisconnectedTime.get() ?: return 0
            lastDisconnected.toEpochMilli() - lastConnected.toEpochMilli()
        }
    }
    
    /**
     * 计算停机时间（毫秒）
     */
    private fun calculateDowntime(): Long {
        val lastDisconnected = lastDisconnectedTime.get() ?: return 0
        return if (currentState.get() == ConnectionState.DISCONNECTED) {
            Instant.now().toEpochMilli() - lastDisconnected.toEpochMilli()
        } else {
            0
        }
    }
}
