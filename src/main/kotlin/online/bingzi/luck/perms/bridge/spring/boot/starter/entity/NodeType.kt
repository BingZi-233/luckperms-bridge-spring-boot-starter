package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

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
    PERMISSION,
    REGEX_PERMISSION,
    INHERITANCE,
    PREFIX,
    SUFFIX,
    META,
    WEIGHT,
    DISPLAY_NAME
} 