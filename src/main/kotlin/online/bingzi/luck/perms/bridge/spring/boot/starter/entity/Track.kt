package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 权限轨道实体类
 *
 * 表示LuckPerms中的一个权限晋升轨道。轨道定义了一系列有序的权限组，
 * 用户可以在这些组之间进行晋升或降级。例如：见习->普通->高级->专家
 *
 * @property name 轨道名称，只能包含小写字母和数字，例如"staff"、"vip"
 * @property groups 组列表，按照晋升顺序排列的权限组名称列表
 */
data class Track(
    @field:NotEmpty
    @field:Pattern(regexp = "^[a-z0-9]+$")
    val name: String,
    
    val groups: List<String> = emptyList()
) 