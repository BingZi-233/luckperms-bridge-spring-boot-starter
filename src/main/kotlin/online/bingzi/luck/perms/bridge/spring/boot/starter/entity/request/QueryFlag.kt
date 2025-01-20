package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 查询标志
 */
enum class QueryFlag {
    /** 解析继承 */
    RESOLVE_INHERITANCE,
    /** 包含没有服务器上下文的节点 */
    INCLUDE_NODES_WITHOUT_SERVER_CONTEXT,
    /** 包含没有世界上下文的节点 */
    INCLUDE_NODES_WITHOUT_WORLD_CONTEXT,
    /** 应用没有服务器上下文的继承节点 */
    APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT,
    /** 应用没有世界上下文的继承节点 */
    APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT
}

/**
 * 默认查询标志
 */
val defaultQueryFlags = listOf(
    QueryFlag.RESOLVE_INHERITANCE,
    QueryFlag.INCLUDE_NODES_WITHOUT_SERVER_CONTEXT,
    QueryFlag.INCLUDE_NODES_WITHOUT_WORLD_CONTEXT,
    QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT,
    QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT
)