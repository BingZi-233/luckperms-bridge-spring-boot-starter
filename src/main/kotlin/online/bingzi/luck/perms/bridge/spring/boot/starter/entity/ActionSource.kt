package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 操作来源实体类
 *
 * 该类用于记录执行操作的来源信息，通常包括管理员或系统的详细信息。
 * 该类的实例包含了唯一标识符和名称，用于标识和描述执行操作的实体。
 *
 * @property uniqueId 来源UUID，必须符合标准UUID格式，通常用于唯一标识来源。
 * @property name 来源名称，表示执行操作的实体的名称，例如管理员的姓名或系统的名称。
 */
data class ActionSource(
    /**
     * 来源UUID，必须符合标准UUID格式。
     * 该属性使用了@NotEmpty注解确保非空，并使用@Pattern注解确保格式正确。
     */
    @field:NotEmpty
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val uniqueId: String,
    
    /**
     * 来源名称，代表执行操作的实体的名称。
     * 该属性使用了@NotEmpty注解确保非空。
     */
    @field:NotEmpty
    val name: String
) 