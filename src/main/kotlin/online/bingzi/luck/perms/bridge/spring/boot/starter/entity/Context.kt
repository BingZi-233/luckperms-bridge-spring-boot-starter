package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern

/**
 * 权限上下文实体类
 *
 * 用于定义权限生效的具体环境。上下文由键值对组成，
 * 例如服务器上下文(server=survival)或世界上下文(world=nether)。
 * 上下文可以组合使用，实现更精细的权限控制。
 *
 * @property key 上下文键，只能包含小写字母和数字，例如"server"、"world"
 * @property value 上下文值，只能包含小写字母和数字，例如"survival"、"nether"
 */
data class Context @JsonCreator constructor(
    @JsonProperty("key")
    @field:Pattern(regexp = "^[a-z0-9]+$")
    val key: String,
    
    @JsonProperty("value")
    @field:Pattern(regexp = "^[a-z0-9]+$")
    val value: String
) 