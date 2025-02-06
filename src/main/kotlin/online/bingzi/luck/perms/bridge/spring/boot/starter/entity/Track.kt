package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 权限轨道实体类
 *
 * 该类表示LuckPerms中的一个权限晋升轨道。轨道定义了一系列有序的权限组，
 * 用户可以在这些组之间进行晋升或降级。例如：见习->普通->高级->专家
 *
 * @property name 轨道名称，只能包含小写字母和数字，例如"staff"、"vip"
 * @property groups 组列表，按照晋升顺序排列的权限组名称列表
 */
data class Track(
    /**
     * 轨道名称
     *
     * 名称应该为非空字符串，并且只能包含小写字母和数字。 
     * 例如有效的名称包括"staff"和"vip"等。
     */
    @field:NotEmpty // 表示该字段不能为空
    @field:Pattern(regexp = "^[a-z0-9]+$") // 表示该字段必须符合特定的正则表达式
    val name: String,
    
    /**
     * 权限组列表
     *
     * 该属性表示一个列表，包含按照晋升顺序排列的权限组名称。
     * 默认值为空列表。列表中的每个元素应为字符串，表示权限组的名称。
     */
    val groups: List<String> = emptyList() // 默认初始化为空列表
) 