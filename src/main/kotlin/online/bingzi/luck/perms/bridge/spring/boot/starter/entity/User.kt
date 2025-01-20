package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 用户实体类
 *
 * 表示LuckPerms中的一个用户（玩家）。用户可以拥有自己的权限节点，
 * 也可以继承自权限组的权限。用户的权限是其自身权限和所属组权限的组合。
 *
 * @property uniqueId 用户UUID，必须符合标准UUID格式
 * @property username 用户名，玩家的游戏ID
 * @property parentGroups 父组列表，用户所属的所有权限组名称
 * @property nodes 权限节点列表，用户自身的权限设置
 * @property metadata 元数据，包含用户的额外信息，如前缀、后缀等
 */
data class User(
    @field:NotEmpty
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val uniqueId: String,

    @field:NotEmpty
    val username: String? = null,

    val parentGroups: List<String> = emptyList(),

    val nodes: List<Node> = emptyList(),

    val metadata: Metadata = Metadata()
) 