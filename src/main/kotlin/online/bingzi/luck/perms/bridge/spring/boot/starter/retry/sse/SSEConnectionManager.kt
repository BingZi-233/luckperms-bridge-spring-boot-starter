package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.sse

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.ConnectionStateType
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.model.stats.ConnectionStats
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicInteger

/**
 * SSE连接管理器
 * 负责管理SSE连接的状态和统计信息
 */
@Component
class SSEConnectionManager {
    private val log = LoggerFactory.getLogger(SSEConnectionManager::class.java)
    private val currentState = AtomicReference(ConnectionStateType.DISCONNECTED)
    private val retryCount = AtomicInteger(0)
    private val lastSuccessTime = AtomicLong(0)
    private val lastFailureTime = AtomicLong(0)
    private val lastResponseTime = AtomicLong(0)

    /**
     * 更新连接状态
     */
    fun updateState(newState: ConnectionStateType) {
        val oldState = currentState.get()
        if (currentState.compareAndSet(oldState, newState)) {
            val currentTime = System.currentTimeMillis()
            when (newState) {
                ConnectionStateType.CONNECTED -> {
                    lastSuccessTime.set(currentTime)
                    lastResponseTime.set(currentTime)
                    if (oldState != ConnectionStateType.CONNECTED) {
                        log.info("SSE连接已建立 - 重试次数: ${retryCount.get()}, 累计运行时间: ${calculateUptime()}ms")
                    }
                }
                ConnectionStateType.DISCONNECTED -> {
                    lastFailureTime.set(currentTime)
                    if (oldState != ConnectionStateType.DISCONNECTED) {
                        log.warn("SSE连接已断开 - 重试次数: ${retryCount.get()}, 累计停机时间: ${calculateDowntime()}ms")
                    }
                }
                ConnectionStateType.CONNECTING -> {
                    log.info("正在建立SSE连接...")
                }
                ConnectionStateType.RETRYING -> {
                    retryCount.incrementAndGet()
                    log.info("正在重试SSE连接 - 第${retryCount.get()}次尝试")
                }
                ConnectionStateType.SUSPENDED -> {
                    lastFailureTime.set(currentTime)
                    log.info("SSE连接已暂停 - 累计运行时间: ${calculateUptime()}ms")
                }
                ConnectionStateType.FAILED -> {
                    lastFailureTime.set(currentTime)
                    log.error("SSE连接已失败 - 重试次数: ${retryCount.get()}, 累计停机时间: ${calculateDowntime()}ms")
                }
                ConnectionStateType.CLOSED -> {
                    lastFailureTime.set(currentTime)
                    log.info("SSE连接已关闭 - 累计运行时间: ${calculateUptime()}ms")
                }
                ConnectionStateType.UNKNOWN -> {
                    log.warn("SSE连接状态未知 - 重试次数: ${retryCount.get()}")
                }
            }
        }
    }

    /**
     * 获取当前状态
     */
    fun getCurrentState(): ConnectionStateType = currentState.get()

    /**
     * 获取重试次数
     */
    fun getRetryCount(): Int = retryCount.get()

    /**
     * 计算累计运行时间
     */
    private fun calculateUptime(): Long {
        val lastSuccess = lastSuccessTime.get()
        return if (lastSuccess > 0) {
            System.currentTimeMillis() - lastSuccess
        } else 0
    }

    /**
     * 计算累计停机时间
     */
    private fun calculateDowntime(): Long {
        val lastFailure = lastFailureTime.get()
        return if (lastFailure > 0) {
            System.currentTimeMillis() - lastFailure
        } else 0
    }

    /**
     * 获取连接统计信息
     */
    fun getConnectionStats(): ConnectionStats = ConnectionStats(
        currentState = currentState.get(),
        retryCount = retryCount.get(),
        uptime = calculateUptime(),
        downtime = calculateDowntime(),
        lastResponseTime = lastResponseTime.get()
    )
}
