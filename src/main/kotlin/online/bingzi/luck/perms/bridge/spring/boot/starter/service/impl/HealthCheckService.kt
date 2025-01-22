package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.HealthApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.config.HealthCheckProperties
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.HealthStatus
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.RetryTemplateFactory
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckManager
import online.bingzi.luck.perms.bridge.spring.boot.starter.retry.health.HealthCheckRetryListener
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class HealthCheckService(
    private val healthApi: HealthApi,
    private val healthCheckProperties: HealthCheckProperties,
    private val healthCheckManager: HealthCheckManager,
    private val healthCheckRetryListener: HealthCheckRetryListener
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "\${luck-perms.health-check.period:30000}")
    fun checkHealth() {
        if (!healthCheckProperties.enabled) {
            return
        }

        val retryTemplate = RetryTemplateFactory.createHealthCheckRetryTemplate(
            maxAttempts = healthCheckProperties.maxAttempts,
            initialInterval = healthCheckProperties.initialInterval,
            multiplier = healthCheckProperties.multiplier,
            maxInterval = healthCheckProperties.maxInterval,
            healthCheckRetryListener = healthCheckRetryListener
        )

        try {
            retryTemplate.execute<Unit, Exception> { context ->
                val startTime = System.currentTimeMillis()
                val response = healthApi.getHealth().execute()
                val responseTime = System.currentTimeMillis() - startTime

                if (response.isSuccessful && response.body() != null) {
                    val healthBody = response.body()!!
                    log.debug("收到健康检查响应: health={}, details={}", healthBody.health, healthBody.details)

                    if (healthBody.health) {
                        healthCheckManager.updateHealth(true, responseTime)
                        log.info("健康检查成功，响应时间: {}ms", responseTime)
                    } else {
                        val reason = healthBody.details["reason"]?.toString() ?: "未知原因"
                        log.error("健康检查返回不健康状态: health={}, details={}", healthBody.health, healthBody.details)
                        healthCheckManager.updateHealth(false, responseTime)
                        throw RuntimeException("健康检查失败: $reason")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    log.error("健康检查请求失败: code={}, error={}", response.code(), errorBody)
                    healthCheckManager.updateHealth(false, -1)
                    throw RuntimeException("健康检查失败: ${errorBody ?: "未知错误"}")
                }
            }
        } catch (e: Exception) {
            log.error("健康检查最终失败: {}", e.message)
            healthCheckManager.updateHealth(false, -1)
        }
    }

    /**
     * 获取当前健康状态
     */
    fun getHealthStatus(): HealthStatus {
        val stats = healthCheckManager.getHealthStats()
        return HealthStatus(
            isHealthy = stats.isHealthy,
            lastCheckTime = stats.lastSuccessTime?.atZone(java.time.ZoneOffset.UTC)?.toLocalDateTime() ?: LocalDateTime.now(),
            lastResponseTime = stats.lastResponseTime ?: -1,
            consecutiveFailures = stats.retryCount,
            downtime = stats.downtime,
            lastFailureTime = stats.lastFailureTime?.atZone(java.time.ZoneOffset.UTC)?.toLocalDateTime()
        )
    }
} 