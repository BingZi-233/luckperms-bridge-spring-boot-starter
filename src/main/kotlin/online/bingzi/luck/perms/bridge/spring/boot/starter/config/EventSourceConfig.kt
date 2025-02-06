package online.bingzi.luck.perms.bridge.spring.boot.starter.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * EventSource配置类
 *
 * 此类用于配置与Server-Sent Events (SSE) 连接相关的参数。
 * 主要功能是将配置文件中的SSE相关属性映射到该类的属性上，
 * 以便在应用程序中使用这些配置。
 *
 * @property connectTimeout 连接超时时间（毫秒），默认值为10000毫秒
 * @property readTimeout 读取超时时间（毫秒），默认值为30000毫秒
 * @property writeTimeout 写入超时时间（毫秒），默认值为10000毫秒
 * @property maxRetries 最大重试次数，默认值为5次
 * @property retryInterval 重试间隔（毫秒），默认值为5000毫秒
 */
@Configuration
@ConfigurationProperties(prefix = "luckperms.bridge.sse")
data class EventSourceConfig(
    /**
     * 连接超时时间
     * @property connectTimeout 连接超时时间（毫秒），用于设置与SSE服务器建立连接的超时时间。
     * 默认值为10000毫秒。
     */
    var connectTimeout: Long = 10000,

    /**
     * 读取超时时间
     * @property readTimeout 读取超时时间（毫秒），用于设置从SSE服务器读取数据的超时时间。
     * 默认值为30000毫秒。
     */
    var readTimeout: Long = 30000,

    /**
     * 写入超时时间
     * @property writeTimeout 写入超时时间（毫秒），用于设置向SSE服务器写入数据的超时时间。
     * 默认值为10000毫秒。
     */
    var writeTimeout: Long = 10000,

    /**
     * 最大重试次数
     * @property maxRetries 最大重试次数，设置在连接失败时的最大重试数量。
     * 默认值为5次。
     */
    var maxRetries: Int = 5,

    /**
     * 重试间隔
     * @property retryInterval 重试间隔（毫秒），设置在连接失败后重试前的等待时间。
     * 默认值为5000毫秒。
     */
    var retryInterval: Long = 5000
) 