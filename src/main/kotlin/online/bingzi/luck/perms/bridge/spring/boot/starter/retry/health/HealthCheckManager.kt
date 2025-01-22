package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * 健康检查状态管理器
 * 负责管理和监控健康检查的状态
 */
@Component
class HealthCheckManager {
    private val log = LoggerFactory.getLogger(HealthCheckManager::class.java)
    
    // 当前健康状态
    private val isHealthy = AtomicReference(true)
    
    // 重试计数器
    private val retryCount = AtomicInteger(0)
    
    // 最后一次成功检查时间
    private val lastSuccessTime = AtomicReference<Instant>()
    
    // 最后一次失败检查时间
    private val lastFailureTime = AtomicReference<Instant>()
    
    // 最后一次响应时间（毫秒）
    private val lastResponseTime = AtomicReference<Long>()
    
    /**
     * 更新健康状态
     */
    fun updateHealth(healthy: Boolean, responseTime: Long) {
        val wasHealthy = isHealthy.get()
        if (isHealthy.compareAndSet(wasHealthy, healthy)) {
            if (healthy) {
                lastSuccessTime.set(Instant.now())
                lastResponseTime.set(responseTime)
                retryCount.set(0)
                log.info("健康检查成功 - 响应时间: ${responseTime}ms")
            } else {
                lastFailureTime.set(Instant.now())
                retryCount.incrementAndGet()
                log.warn("健康检查失败 - 重试次数: ${retryCount.get()}")
            }
        }
    }
    
    /**
     * 获取当前健康状态
     */
    fun isHealthy(): Boolean = isHealthy.get()
    
    /**
     * 获取当前重试次数
     */
    fun getRetryCount(): Int = retryCount.get()
    
    /**
     * 获取健康检查统计信息
     */
    fun getHealthStats(): HealthStats {
        return HealthStats(
            isHealthy = isHealthy.get(),
            retryCount = retryCount.get(),
            lastSuccessTime = lastSuccessTime.get(),
            lastFailureTime = lastFailureTime.get(),
            lastResponseTime = lastResponseTime.get(),
            uptime = calculateUptime(),
            downtime = calculateDowntime()
        )
    }
    
    /**
     * 计算正常运行时间（毫秒）
     */
    private fun calculateUptime(): Long {
        val lastSuccess = lastSuccessTime.get() ?: return 0
        return if (isHealthy.get()) {
            Instant.now().toEpochMilli() - lastSuccess.toEpochMilli()
        } else {
            val lastFailure = lastFailureTime.get() ?: return 0
            lastFailure.toEpochMilli() - lastSuccess.toEpochMilli()
        }
    }
    
    /**
     * 计算停机时间（毫秒）
     */
    private fun calculateDowntime(): Long {
        val lastFailure = lastFailureTime.get() ?: return 0
        return if (!isHealthy.get()) {
            Instant.now().toEpochMilli() - lastFailure.toEpochMilli()
        } else {
            0
        }
    }
}

/**
 * 健康检查统计信息
 */
data class HealthStats(
    val isHealthy: Boolean,
    val retryCount: Int,
    val lastSuccessTime: Instant?,
    val lastFailureTime: Instant?,
    val lastResponseTime: Long?,
    val uptime: Long,
    val downtime: Long
) 