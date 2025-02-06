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

/**
 * 健康检查服务类
 *
 * 该类负责定期检查系统的健康状态，并记录健康检查的结果。
 * 主要功能包括执行健康检查、处理健康检查的响应结果以及提供当前健康状态的信息。
 */
@Service
class HealthCheckService(
    private val healthApi: HealthApi, // 用于调用健康检查API的接口
    private val healthCheckProperties: HealthCheckProperties, // 健康检查的配置信息
    private val healthCheckManager: HealthCheckManager, // 健康检查状态管理器
    private val healthCheckRetryListener: HealthCheckRetryListener // 健康检查重试监听器
) {
    private val log = LoggerFactory.getLogger(this::class.java) // 日志记录器

    /**
     * 定期进行健康检查
     *
     * 该方法使用Spring的@Scheduled注解定时执行健康检查。
     * 如果健康检查被禁用，则直接返回。
     * 否则，创建重试模板并执行健康检查请求。
     * 在成功响应的情况下，更新健康状态；在失败时，记录错误并更新健康状态为不健康。
     */
    @Scheduled(fixedDelayString = "\${luck-perms.health-check.period:30000}")
    fun checkHealth() {
        if (!healthCheckProperties.enabled) { // 检查健康检查是否被启用
            return // 如果未启用，直接返回
        }

        // 创建健康检查重试模板
        val retryTemplate = RetryTemplateFactory.createHealthCheckRetryTemplate(
            maxAttempts = healthCheckProperties.maxAttempts, // 最大重试次数
            initialInterval = healthCheckProperties.initialInterval, // 初始重试间隔
            multiplier = healthCheckProperties.multiplier, // 重试间隔的倍数
            maxInterval = healthCheckProperties.maxInterval, // 最大重试间隔
            healthCheckRetryListener = healthCheckRetryListener // 重试监听器
        )

        try {
            // 执行重试模板，进行健康检查
            retryTemplate.execute<Unit, Exception> { context ->
                val startTime = System.currentTimeMillis() // 记录开始时间
                val response = healthApi.getHealth().execute() // 执行健康检查请求
                val responseTime = System.currentTimeMillis() - startTime // 计算响应时间

                if (response.isSuccessful && response.body() != null) { // 检查响应是否成功且非空
                    val healthBody = response.body()!! // 获取响应体
                    log.debug("收到健康检查响应: health={}, details={}", healthBody.health, healthBody.details) // 记录调试信息

                    if (healthBody.health) { // 检查健康状态
                        healthCheckManager.updateHealth(true, responseTime) // 更新健康状态为健康
                        log.info("健康检查成功，响应时间: {}ms", responseTime) // 记录信息日志
                    } else { // 如果健康状态不健康
                        val reason = healthBody.details["reason"]?.toString() ?: "未知原因" // 获取失败原因
                        log.error("健康检查返回不健康状态: health={}, details={}", healthBody.health, healthBody.details) // 记录错误日志
                        healthCheckManager.updateHealth(false, responseTime) // 更新健康状态为不健康
                        throw RuntimeException("健康检查失败: $reason") // 抛出运行时异常
                    }
                } else { // 如果响应不成功
                    val errorBody = response.errorBody()?.string() // 获取错误响应体
                    log.error("健康检查请求失败: code={}, error={}", response.code(), errorBody) // 记录错误日志
                    healthCheckManager.updateHealth(false, -1) // 更新健康状态为不健康
                    throw RuntimeException("健康检查失败: ${errorBody ?: "未知错误"}") // 抛出运行时异常
                }
            }
        } catch (e: Exception) { // 捕获异常
            log.error("健康检查最终失败: {}", e.message) // 记录最终失败日志
            healthCheckManager.updateHealth(false, -1) // 更新健康状态为不健康
        }
    }

    /**
     * 获取当前健康状态
     *
     * @return HealthStatus 当前的健康状态信息，包括是否健康、最后检查时间等。
     */
    fun getHealthStatus(): HealthStatus {
        val stats = healthCheckManager.getHealthStats() // 获取健康状态统计信息
        return HealthStatus(
            isHealthy = stats.isHealthy, // 健康状态
            lastCheckTime = stats.lastSuccessTime?.atZone(java.time.ZoneOffset.UTC)?.toLocalDateTime() ?: LocalDateTime.now(), // 最后检查时间
            lastResponseTime = stats.lastResponseTime ?: -1, // 最后响应时间
            consecutiveFailures = stats.retryCount, // 连续失败次数
            downtime = stats.downtime, // 停机时间
            lastFailureTime = stats.lastFailureTime?.atZone(java.time.ZoneOffset.UTC)?.toLocalDateTime() // 最后失败时间
        )
    }
}