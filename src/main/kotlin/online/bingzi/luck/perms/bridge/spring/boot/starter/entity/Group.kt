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
    @field:NotEmpty
    @field:Pattern(regexp = "^[a-z0-9]+$")
    val name: String,

    val displayName: String? = null,

    val weight: Int? = null,

    val nodes: List<Node> = emptyList(),

    val metadata: Metadata = Metadata()
)