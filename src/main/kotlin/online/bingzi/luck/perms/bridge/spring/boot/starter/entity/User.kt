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
    /**
     * 用户的唯一标识符，UUID格式的字符串。
     * 必须符合标准UUID格式，例如"123e4567-e89b-12d3-a456-426614174000"。
     */
    @field:NotEmpty
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val uniqueId: String,

    /**
     * 用户的游戏ID，表示用户的名称。
     * 这个字段是可选的，可以为null。
     */
    @field:NotEmpty
    val username: String? = null,

    /**
     * 用户所属的权限组名称列表。
     * 该列表包含所有用户的父组，可以为空列表。
     */
    val parentGroups: List<String> = emptyList(),

    /**
     * 用户自身的权限节点列表。
     * 该列表包含用户直接拥有的权限节点，可以为空列表。
     */
    val nodes: List<Node> = emptyList(),

    /**
     * 用户的元数据，包含额外的用户信息。
     * 默认初始化为一个空的Metadata对象。
     */
    val metadata: Metadata = Metadata()
) 