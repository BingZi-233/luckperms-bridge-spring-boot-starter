package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * 操作目标实体类
 *
 * 该类用于记录被操作的对象信息，可能的对象包括用户、组或轨道。
 * 通过这个类，我们能够方便地管理和识别在权限操作中涉及的对象。
 *
 * @property uniqueId 目标对象的唯一标识符（UUID），如果目标是用户，则必须符合标准UUID格式。
 * @property name 被操作对象的名称，不能为空。
 * @property type 被操作对象的类型，表示对象是用户、组还是轨道。
 */
data class ActionTarget(
    /**
     * 目标对象的唯一标识符，必须符合UUID的正则表达式格式。
     * 例如：'123e4567-e89b-12d3-a456-426614174000'
     */
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val uniqueId: String? = null,

    /**
     * 被操作对象的名称，该字段不能为空。
     * 例如：'管理员用户'或'开发组'
     */
    @field:NotEmpty
    val name: String,

    /**
     * 被操作对象的类型，表示该对象是用户、组或轨道。
     * 该属性的类型为ActionTargetType，具体的类型定义将影响操作的逻辑。
     */
    val type: ActionTargetType
)