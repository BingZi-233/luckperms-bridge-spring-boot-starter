package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 查询标志枚举类
 * 
 * 该枚举类定义了一系列的查询标志，用于控制查询操作的行为。每个枚举值表示一种特定的查询选项。
 * 主要功能是为查询操作提供灵活的配置选项，以便在不同场景下使用合适的查询策略。
 */
enum class QueryFlag {
    /** 
     * 解析继承标志
     * 
     * 此标志用于指示是否解析继承关系。
     */
    RESOLVE_INHERITANCE,

    /** 
     * 包含没有服务器上下文的节点标志
     * 
     * 此标志用于指示查询结果中是否包含没有服务器上下文的节点。
     */
    INCLUDE_NODES_WITHOUT_SERVER_CONTEXT,

    /** 
     * 包含没有世界上下文的节点标志
     * 
     * 此标志用于指示查询结果中是否包含没有世界上下文的节点。
     */
    INCLUDE_NODES_WITHOUT_WORLD_CONTEXT,

    /** 
     * 应用没有服务器上下文的继承节点标志
     * 
     * 此标志用于指示在查询中是否应用那些没有服务器上下文的继承节点。
     */
    APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT,

    /** 
     * 应用没有世界上下文的继承节点标志
     * 
     * 此标志用于指示在查询中是否应用那些没有世界上下文的继承节点。
     */
    APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT
}

/**
 * 默认查询标志列表
 * 
 * 该列表包含了一组默认的查询标志，这些标志将被用于初始化查询操作的配置。
 * 主要用于提供一个标准的查询行为，确保在没有特殊配置的情况下，查询操作能够正常进行。
 */
val defaultQueryFlags = listOf(
    QueryFlag.RESOLVE_INHERITANCE, // 解析继承
    QueryFlag.INCLUDE_NODES_WITHOUT_SERVER_CONTEXT, // 包含没有服务器上下文的节点
    QueryFlag.INCLUDE_NODES_WITHOUT_WORLD_CONTEXT, // 包含没有世界上下文的节点
    QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT, // 应用没有服务器上下文的继承节点
    QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT // 应用没有世界上下文的继承节点
)