package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern

/**
 * 权限上下文实体类
 *
 * 该类用于定义权限生效的具体环境。上下文由一对键值组成，
 * 例如服务器上下文(server=survival)或世界上下文(world=nether)。
 * 上下文可以组合使用，从而实现更精细的权限控制。
 *
 * 主要功能包括：
 * - 存储上下文的键和值
 * - 验证键和值格式的合法性
 *
 * @property key 上下文键，只能包含小写字母和数字，例如"server"、"world"
 * @property value 上下文值，只能包含小写字母和数字，例如"survival"、"nether"
 */
data class Context @JsonCreator constructor(
    /**
     * 上下文键，表示具体的上下文类型。
     * 例如，可以是"server"表示服务器，"world"表示世界。
     * 该值必须符合正则表达式^[a-z0-9]+$，即只能包含小写字母和数字。
     */
    @JsonProperty("key")
    @field:Pattern(regexp = "^[a-z0-9]+$")
    val key: String,
    
    /**
     * 上下文值，表示上下文的具体状态或环境。
     * 例如，可以是"survival"表示生存模式，"nether"表示地狱世界。
     * 该值必须符合正则表达式^[a-z0-9]+$，即只能包含小写字母和数字。
     */
    @JsonProperty("value")
    @field:Pattern(regexp = "^[a-z0-9]+$")
    val value: String
) 