package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.HealthApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.config.HealthCheckProperties
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.HealthStatus
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@Service
class HealthCheckService(
    private val healthApi: HealthApi,
    private val healthCheckProperties: HealthCheckProperties
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val healthStatus = AtomicReference(HealthStatus())

    @Scheduled(fixedDelayString = "\${luck-perms.health-check.period:30000}")
    fun checkHealth() {
        if (!healthCheckProperties.enabled) {
            return
        }

        val startTime = System.currentTimeMillis()
        try {
            // 使用 HealthApi 进行健康检查
            val response = healthApi.getHealth().execute()
            
            val responseTime = System.currentTimeMillis() - startTime
            
            if (response.isSuccessful && response.body() != null) {
                val healthBody = response.body()!!
                log.info("收到健康检查响应: health={}, details={}", healthBody.health, healthBody.details)
                
                if (healthBody.health) {
                    updateHealthyStatus(responseTime)
                    log.info("健康检查成功，响应时间: {}ms", responseTime)
                } else {
                    val reason = healthBody.details["reason"]?.toString() ?: "未知原因"
                    log.error("健康检查返回不健康状态: health={}, details={}", healthBody.health, healthBody.details)
                    throw RuntimeException("健康检查失败: $reason")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                log.error("健康检查请求失败: code={}, error={}", response.code(), errorBody)
                throw RuntimeException("健康检查失败: ${errorBody ?: "未知错误"}")
            }
        } catch (e: Exception) {
            updateUnhealthyStatus()
            log.error("健康检查发生异常: {}", e.message, e)
        }
    }

    private fun updateHealthyStatus(responseTime: Long) {
        healthStatus.updateAndGet { currentStatus ->
            currentStatus.copy(
                isHealthy = true,
                lastCheckTime = LocalDateTime.now(),
                lastResponseTime = responseTime,
                consecutiveFailures = 0,
                downtime = if (currentStatus.lastFailureTime != null) {
                    currentStatus.downtime + ChronoUnit.MILLIS.between(currentStatus.lastFailureTime, LocalDateTime.now())
                } else {
                    currentStatus.downtime
                },
                lastFailureTime = null
            )
        }
    }

    private fun updateUnhealthyStatus() {
        healthStatus.updateAndGet { currentStatus ->
            currentStatus.copy(
                isHealthy = false,
                lastCheckTime = LocalDateTime.now(),
                consecutiveFailures = currentStatus.consecutiveFailures + 1,
                lastFailureTime = currentStatus.lastFailureTime ?: LocalDateTime.now()
            )
        }
    }

    /**
     * 获取当前健康状态
     */
    fun getHealthStatus(): HealthStatus = healthStatus.get()
} 