package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.health

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 健康状态实体类
 * 用于表示LuckPerms系统的健康状态
 *
 * @property health 系统是否健康
 *                true: 系统正常运行
 *                false: 系统存在问题
 * @property details 健康检查的详细信息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Health @JsonCreator constructor(
    /**
     * 系统是否健康
     */
    @JsonProperty("health")
    val health: Boolean,

    /**
     * 健康检查的详细信息
     */
    @JsonProperty("details")
    val details: Map<String, Any>
) 