package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.health

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 健康状态实体类
 * 用于表示LuckPerms系统的健康状态
 *
 * @property health 系统是否健康
 *                true: 系统正常运行
 *                false: 系统存在问题
 * @property details 健康状态详细信息
 *                 包含具体的健康检查结果，如：
 *                 - storageConnected: 存储连接状态
 *                 - storagePing: 存储延迟
 *                 - reason: 当health为false时的原因说明
 */
data class Health(
    @JsonProperty("health")
    val health: Boolean,
    @JsonProperty("details")
    val details: Map<String, Any>
) 