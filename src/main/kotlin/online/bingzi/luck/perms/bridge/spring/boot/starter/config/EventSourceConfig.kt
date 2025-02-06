package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * EventSource配置类
 *
 * 用于配置SSE连接的相关参数
 *
 * @property connectTimeout 连接超时时间（毫秒）
 * @property readTimeout 读取超时时间（毫秒）
 * @property writeTimeout 写入超时时间（毫秒）
 * @property maxRetries 最大重试次数
 * @property retryInterval 重试间隔（毫秒）
 */
@Configuration
@ConfigurationProperties(prefix = "luckperms.bridge.sse")
data class EventSourceConfig(
    var connectTimeout: Long = 10000,
    var readTimeout: Long = 30000,
    var writeTimeout: Long = 10000,
    var maxRetries: Int = 5,
    var retryInterval: Long = 5000
) 