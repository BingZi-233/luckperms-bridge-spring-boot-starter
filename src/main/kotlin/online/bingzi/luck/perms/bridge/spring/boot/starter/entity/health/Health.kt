package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.health

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 健康状态实体类
 * 此类用于表示LuckPerms系统的健康状态，包含系统的健康标志和详细信息。
 *
 * @property health 系统是否健康
 *                true: 系统正常运行
 *                false: 系统存在问题
 * @property details 健康检查的详细信息，以键值对的形式存储
 */
@JsonIgnoreProperties(ignoreUnknown = true)  // 忽略未知属性，以便于反序列化时不抛出异常
data class Health @JsonCreator constructor(
    /**
     * 系统是否健康
     * 表示系统当前的运行状态。
     */
    @JsonProperty("health")  // 将JSON中的"health"属性映射到此属性
    val health: Boolean,

    /**
     * 健康检查的详细信息
     * 包含与健康状态相关的额外信息，键为信息名称，值为对应的信息内容。
     */
    @JsonProperty("details")  // 将JSON中的"details"属性映射到此属性
    val details: Map<String, Any>  // 使用Map存储详细信息，键为String类型，值为任意类型
)  // 数据类的结束标记