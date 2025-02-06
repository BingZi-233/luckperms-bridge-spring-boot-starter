package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotEmpty

/**
 * 权限节点实体类
 *
 * 该类用于表示LuckPerms中的一个权限节点。权限节点不仅包含权限分配，
 * 还用于存储继承的组、前缀、后缀和元数据值等信息。
 *
 * 权限节点的关键属性包括权限键、节点类型、节点值、上下文集合以及过期时间。
 * 这些信息共同定义了节点的行为和生效条件。
 *
 * @property key 权限键，表示具体的权限标识符，例如"minecraft.command.ban"
 * @property type 节点类型，表示该节点的功能类型，如普通权限、正则权限、继承等
 * @property value 节点值，true表示允许，false表示拒绝，默认为true
 * @property context 上下文集合，定义权限生效的具体环境，如服务器、世界等
 * @property expiry 过期时间(Unix时间戳，单位：秒)，null表示永不过期
 */
data class Node @JsonCreator constructor(
    /**
     * 权限键，表示具体的权限标识符，例如"minecraft.command.ban"
     * 该属性不能为空，使用了@NotEmpty注解进行验证
     */
    @JsonProperty("key")
    @field:NotEmpty
    val key: String,

    /**
     * 节点类型，表示该节点的功能类型。
     * 可能的取值包括各种节点类型，如普通权限、正则权限、继承等。
     */
    @JsonProperty("type")
    val type: NodeType,

    /**
     * 节点值，表示该权限的状态。
     * true表示允许，false表示拒绝，默认为true。
     */
    @JsonProperty("value")
    val value: Boolean = true,

    /**
     * 上下文集合，定义权限生效的具体环境。
     * 这里可以包含多个上下文，例如服务器名、世界名等。
     * 默认为空列表，表示没有特定上下文限制。
     */
    @JsonProperty("context")
    val context: List<Context> = emptyList(),

    /**
     * 过期时间，使用Unix时间戳表示，单位为秒。
     * 值为null表示该权限节点永不过期。
     */
    @JsonProperty("expiry")
    val expiry: Long? = null
)