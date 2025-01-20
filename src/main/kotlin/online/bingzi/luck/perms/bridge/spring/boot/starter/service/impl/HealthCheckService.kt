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

@Service
class HealthCheckService(
    private val healthApi: HealthApi,
    private val healthCheckProperties: HealthCheckProperties
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    private val healthStatus = HealthStatus()

    @Scheduled(fixedDelayString = "#{@healthCheckProperties.period.toMillis()}")
    fun checkHealth() {
        if (!healthCheckProperties.enabled) {
            return
        }

        val startTime = System.currentTimeMillis()
        try {
            // 使用 HealthApi 进行健康检查
            val response = healthApi.getHealth().execute()
            
            val responseTime = System.currentTimeMillis() - startTime
            
            if (response.isSuccessful && response.body()?.isHealthy == true) {
                updateHealthyStatus(responseTime)
                log.info("健康检查成功，响应时间: {}ms", responseTime)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "未知错误"
                throw RuntimeException("健康检查失败: $errorMessage")
            }
        } catch (e: Exception) {
            updateUnhealthyStatus()
            log.error("健康检查失败: {}", e.message)
        }
    }

    private fun updateHealthyStatus(responseTime: Long) {
        healthStatus.apply {
            isHealthy = true
            lastCheckTime = LocalDateTime.now()
            lastResponseTime = responseTime
            consecutiveFailures = 0
            if (lastFailureTime != null) {
                downtime += ChronoUnit.MILLIS.between(lastFailureTime, LocalDateTime.now())
                lastFailureTime = null
            }
        }
    }

    private fun updateUnhealthyStatus() {
        healthStatus.apply {
            isHealthy = false
            lastCheckTime = LocalDateTime.now()
            consecutiveFailures++
            if (lastFailureTime == null) {
                lastFailureTime = LocalDateTime.now()
            }
        }
    }

    /**
     * 获取当前健康状态
     */
    fun getHealthStatus(): HealthStatus = healthStatus.copy()
} 