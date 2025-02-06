package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 权限组实体类
 *
 * 表示LuckPerms中的一个权限组。权限组是一组权限的集合，
 * 可以被分配给玩家。组可以继承其他组的权限，形成权限继承体系。
 *
 * @property name 组名，只能包含小写字母和数字，例如"admin"、"vip"
 * @property displayName 显示名称，用于展示的友好名称，例如"管理员"、"VIP玩家"
 * @property weight 权重，决定组的优先级，数值越大优先级越高
 * @property nodes 权限节点列表，包含该组所有的权限设置
 * @property metadata 元数据，包含组的额外信息，如前缀、后缀等
 */
data class Group(
    /**
     * 组名，必须非空且只能包含小写字母和数字。
     * 例如，合法的组名包括"admin"、"vip"等。
     */
    @field:NotEmpty
    @field:Pattern(regexp = "^[a-z0-9]+$")
    val name: String,

    /**
     * 显示名称，用于展示的友好名称。
     * 可以为null，表示没有特别的显示名称。
     * 例如，可以是"管理员"或"VIP玩家"。
     */
    val displayName: String? = null,

    /**
     * 权重值，决定该组的优先级。
     * 数值越大，优先级越高。可以为null，表示没有特别的权重设定。
     */
    val weight: Int? = null,

    /**
     * 权限节点列表，保存该组所有的权限设置。
     * 默认值为空列表，表示该组没有权限节点。
     */
    val nodes: List<Node> = emptyList(),

    /**
     * 元数据，保存组的额外信息，如前缀、后缀等。
     * 默认值是一个空的Metadata对象。
     */
    val metadata: Metadata = Metadata()
)