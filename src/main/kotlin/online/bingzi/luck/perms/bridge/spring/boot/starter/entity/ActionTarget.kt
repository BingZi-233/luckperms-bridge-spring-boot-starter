package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 操作目标实体类
 *
 * 记录被操作对象（可以是用户、组或轨道）的详细信息。
 *
 * @property uniqueId 目标UUID，如果目标是用户则必须符合标准UUID格式
 * @property name 目标名称，被操作对象的名称
 * @property type 目标类型，表明被操作对象是用户、组还是轨道
 */
data class ActionTarget(
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val uniqueId: String? = null,
    
    @field:NotEmpty
    val name: String,
    
    val type: ActionTargetType
) 