package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums

/**
 * 权限查询标志
 */
enum class QueryFlag {
    /**
     * 解析继承关系
     */
    RESOLVE_INHERITANCE,

    /**
     * 包含没有服务器上下文的节点
     */
    INCLUDE_NODES_WITHOUT_SERVER_CONTEXT,

    /**
     * 包含没有世界上下文的节点
     */
    INCLUDE_NODES_WITHOUT_WORLD_CONTEXT,

    /**
     * 应用没有服务器上下文的继承节点
     */
    APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT,

    /**
     * 应用没有世界上下文的继承节点
     */
    APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT
} 