package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotEmpty

/**
 * 权限节点实体类
 *
 * 用于表示LuckPerms中的一个权限节点。权限节点不仅包含权限分配，
 * 还用于存储继承的组、前缀、后缀和元数据值等信息。
 *
 * @property key 权限键，表示具体的权限标识符，例如"minecraft.command.ban"
 * @property type 节点类型，表示该节点的功能类型，如普通权限、正则权限、继承等
 * @property value 节点值，true表示允许，false表示拒绝，默认为true
 * @property context 上下文集合，定义权限生效的具体环境，如服务器、世界等
 * @property expiry 过期时间(Unix时间戳，单位：秒)，null表示永不过期
 */
data class Node @JsonCreator constructor(
    @JsonProperty("key")
    @field:NotEmpty
    val key: String,

    @JsonProperty("type")
    val type: NodeType,

    @JsonProperty("value")
    val value: Boolean = true,

    @JsonProperty("context")
    val context: List<Context> = emptyList(),

    @JsonProperty("expiry")
    val expiry: Long? = null
)