package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * 节点类型枚举
 *
 * 定义了LuckPerms中所有可能的节点类型：
 * - PERMISSION: 普通权限节点
 * - REGEX_PERMISSION: 正则表达式权限节点
 * - INHERITANCE: 继承节点，用于继承其他组的权限
 * - PREFIX: 前缀节点，用于设置显示前缀
 * - SUFFIX: 后缀节点，用于设置显示后缀
 * - META: 元数据节点，用于存储自定义数据
 * - WEIGHT: 权重节点，用于设置组的优先级
 * - DISPLAY_NAME: 显示名称节点，用于设置显示名称
 */
enum class NodeType {
    PERMISSION, // 普通权限节点
    REGEX_PERMISSION, // 正则表达式权限节点
    INHERITANCE, // 继承节点，表示该节点将继承其他节点的权限
    PREFIX, // 前缀节点，用于设置和显示前缀
    SUFFIX, // 后缀节点，用于设置和显示后缀
    META, // 元数据节点，用于存储额外的自定义数据
    WEIGHT, // 权重节点，用于定义组的优先级
    DISPLAY_NAME; // 显示名称节点，用于设置该节点的显示名称

    /**
     * 将当前节点类型转换为小写字符串
     * 
     * @return 当前节点类型的小写名称字符串
     */
    @JsonValue
    fun toValue(): String = name.lowercase()

    companion object {
        /**
         * 从给定的字符串值创建对应的节点类型
         * 
         * @param value 节点类型的字符串表示，必须是有效的节点类型名称
         * @return 对应的 NodeType 枚举值
         * @throws IllegalArgumentException 如果提供的字符串不匹配任何节点类型
         */
        @JsonCreator
        @JvmStatic
        fun fromValue(value: String): NodeType = valueOf(value.uppercase())
    }
}