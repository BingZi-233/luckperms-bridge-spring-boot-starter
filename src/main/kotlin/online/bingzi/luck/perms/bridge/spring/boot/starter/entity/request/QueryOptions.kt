package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Context
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.QueryFlag
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.QueryMode

/**
 * 查询选项类
 *
 * 此类用于封装查询的选项，包括查询模式、标志以及上下文集合。
 * 通过设置不同的选项，可以定制查询的行为和结果。
 *
 * @property mode 查询模式，默认为CONTEXTUAL，指明查询的上下文方式。
 * @property flags 查询标志，使用集合存储多个标志，以影响查询的具体执行。
 * @property contexts 上下文集合，包含了查询过程中使用的上下文信息。
 */
data class QueryOptions(
    val mode: QueryMode = QueryMode.CONTEXTUAL, // 查询模式，默认为上下文模式
    val flags: Set<QueryFlag> = defaultFlags, // 查询标志，使用默认标志集合
    val contexts: List<Context> = emptyList() // 上下文集合，初始为空列表
) {
    companion object {
        // 默认查询标志集合，包含多个标志以支持不同的查询需求
        private val defaultFlags = setOf(
            QueryFlag.RESOLVE_INHERITANCE, // 解析继承关系
            QueryFlag.INCLUDE_NODES_WITHOUT_SERVER_CONTEXT, // 包括没有服务器上下文的节点
            QueryFlag.INCLUDE_NODES_WITHOUT_WORLD_CONTEXT, // 包括没有世界上下文的节点
            QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT, // 应用没有服务器上下文的继承节点
            QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT // 应用没有世界上下文的继承节点
        )
    }
}