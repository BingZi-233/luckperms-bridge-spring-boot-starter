package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums

/**
 * 枚举类：权限查询标志
 * 
 * 该枚举定义了一组用于权限查询的标志，用于控制在查询权限时
 * 的行为和结果。这些标志可以在权限管理和访问控制的场景中使用，
 * 以便更灵活地处理不同的查询需求。
 */
enum class QueryFlag {
    
    /**
     * 标志：解析继承关系
     * 
     * 当此标志被设置时，查询将会考虑权限的继承关系，
     * 允许用户获取与其权限相关的所有继承的权限。
     */
    RESOLVE_INHERITANCE,

    /**
     * 标志：包含没有服务器上下文的节点
     * 
     * 当此标志被设置时，查询结果将包括那些不依赖于
     * 服务器上下文的节点，这对于全局权限的查询是有用的。
     */
    INCLUDE_NODES_WITHOUT_SERVER_CONTEXT,

    /**
     * 标志：包含没有世界上下文的节点
     * 
     * 当此标志被设置时，查询结果将包括那些不依赖于
     * 世界上下文的节点，这在某些特定的权限检查场景中
     * 是必要的。
     */
    INCLUDE_NODES_WITHOUT_WORLD_CONTEXT,

    /**
     * 标志：应用没有服务器上下文的继承节点
     * 
     * 当此标志被设置时，查询将会应用那些没有服务器上下文的
     * 继承节点，确保即使在缺少服务器上下文的情况下也能
     * 识别相关权限。
     */
    APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT,

    /**
     * 标志：应用没有世界上下文的继承节点
     * 
     * 当此标志被设置时，查询将会应用那些没有世界上下文的
     * 继承节点，以保证在缺乏世界上下文的情况下，
     * 仍能正确地处理权限的继承关系。
     */
    APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT
}