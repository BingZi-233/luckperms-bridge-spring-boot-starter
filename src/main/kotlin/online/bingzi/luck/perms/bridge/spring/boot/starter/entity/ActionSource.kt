package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 操作来源实体类
 *
 * 记录执行操作的实体（通常是管理员或系统）的详细信息。
 *
 * @property uniqueId 来源UUID，必须符合标准UUID格式
 * @property name 来源名称，执行操作的实体名称
 */
data class ActionSource(
    @field:NotEmpty
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val uniqueId: String,
    
    @field:NotEmpty
    val name: String
) 