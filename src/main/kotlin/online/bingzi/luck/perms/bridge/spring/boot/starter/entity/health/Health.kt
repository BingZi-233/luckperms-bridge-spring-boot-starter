package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.health

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 健康状态实体类
 * 用于表示LuckPerms系统的健康状态
 *
 * @property isHealthy 系统是否健康
 *                true: 系统正常运行
 *                false: 系统存在问题
 * @property storageConnected 存储系统连接状态
 * @property storagePing 存储系统响应时间（毫秒）
 * @property issues 问题描述（如果存在）
 */
data class Health(
    /**
     * 系统是否健康
     */
    @JsonProperty("healthy")
    val isHealthy: Boolean,

    /**
     * 存储系统连接状态
     */
    @JsonProperty("storage_connected")
    val storageConnected: Boolean,

    /**
     * 存储系统响应时间（毫秒）
     */
    @JsonProperty("storage_ping")
    val storagePing: Long,

    /**
     * 问题描述（如果存在）
     */
    @JsonProperty("issues")
    val issues: List<String>? = null
) 