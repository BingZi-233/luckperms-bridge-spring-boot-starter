package online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 * 健康检查状态管理器
 * 该类负责管理和监控系统的健康检查状态，包括健康状态的更新、重试计数以及相关的时间戳。
 */
@Component
class HealthCheckManager {
    // 使用 SLF4J 记录日志
    private val log = LoggerFactory.getLogger(HealthCheckManager::class.java)
    
    // 当前健康状态，初始为健康（true）
    private val isHealthy = AtomicReference(true)
    
    // 记录重试次数，初始为 0
    private val retryCount = AtomicInteger(0)
    
    // 最后一次成功健康检查的时间
    private val lastSuccessTime = AtomicReference<Instant>()
    
    // 最后一次失败健康检查的时间
    private val lastFailureTime = AtomicReference<Instant>()
    
    // 最后一次响应的时间（毫秒）
    private val lastResponseTime = AtomicReference<Long>()
    
    /**
     * 更新健康状态
     * 
     * @param healthy 当前的健康状态，true 表示健康，false 表示不健康
     * @param responseTime 上一次健康检查的响应时间，单位为毫秒
     */
    fun updateHealth(healthy: Boolean, responseTime: Long) {
        // 获取当前健康状态
        val wasHealthy = isHealthy.get()
        // 更新健康状态，如果状态发生改变，则执行以下逻辑
        if (isHealthy.compareAndSet(wasHealthy, healthy)) {
            // 如果当前状态是健康
            if (healthy) {
                // 记录成功时间和响应时间
                lastSuccessTime.set(Instant.now())
                lastResponseTime.set(responseTime)
                // 重试计数归零
                retryCount.set(0)
                // 记录成功日志
                log.info("健康检查成功 - 响应时间: ${responseTime}ms")
            } else {
                // 如果当前状态是不健康，记录失败时间并增加重试计数
                lastFailureTime.set(Instant.now())
                retryCount.incrementAndGet()
                // 记录失败日志
                log.warn("健康检查失败 - 重试次数: ${retryCount.get()}")
            }
        }
    }
    
    /**
     * 获取当前健康状态
     * 
     * @return 返回当前的健康状态，true 表示健康，false 表示不健康
     */
    fun isHealthy(): Boolean = isHealthy.get()
    
    /**
     * 获取当前重试次数
     * 
     * @return 返回当前的重试次数
     */
    fun getRetryCount(): Int = retryCount.get()
    
    /**
     * 获取健康检查统计信息
     * 
     * @return 返回一个包含健康状态、重试次数和时间戳等信息的 HealthStats 对象
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
     * 
     * @return 返回从最后一次成功检查到现在的时间，若未成功检查则返回失败时间与成功时间的差值
     */
    private fun calculateUptime(): Long {
        // 获取最后一次成功检查的时间
        val lastSuccess = lastSuccessTime.get() ?: return 0
        return if (isHealthy.get()) {
            // 如果当前健康，则计算从成功检查到现在的时间
            Instant.now().toEpochMilli() - lastSuccess.toEpochMilli()
        } else {
            // 如果不健康，计算失败时间与成功时间的差值
            val lastFailure = lastFailureTime.get() ?: return 0
            lastFailure.toEpochMilli() - lastSuccess.toEpochMilli()
        }
    }
    
    /**
     * 计算停机时间（毫秒）
     * 
     * @return 返回从最后一次失败检查到现在的时间，如果当前健康则返回 0
     */
    private fun calculateDowntime(): Long {
        // 获取最后一次失败检查的时间
        val lastFailure = lastFailureTime.get() ?: return 0
        return if (!isHealthy.get()) {
            // 如果当前不健康，计算从失败检查到现在的时间
            Instant.now().toEpochMilli() - lastFailure.toEpochMilli()
        } else {
            // 如果健康，返回 0
            0
        }
    }
}

/**
 * 健康检查统计信息
 * 
 * @property isHealthy 当前健康状态，true 表示健康，false 表示不健康
 * @property retryCount 当前重试次数
 * @property lastSuccessTime 最后一次成功检查的时间
 * @property lastFailureTime 最后一次失败检查的时间
 * @property lastResponseTime 最后一次响应的时间（毫秒）
 * @property uptime 正常运行时间（毫秒）
 * @property downtime 停机时间（毫秒）
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