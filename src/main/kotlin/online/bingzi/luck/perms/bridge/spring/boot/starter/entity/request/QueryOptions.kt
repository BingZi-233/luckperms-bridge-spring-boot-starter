package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Context
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.QueryFlag
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums.QueryMode

/**
 * 查询选项
 *
 * @property mode 查询模式，默认为CONTEXTUAL
 * @property flags 查询标志
 * @property contexts 上下文集合
 */
data class QueryOptions(
    val mode: QueryMode = QueryMode.CONTEXTUAL,
    val flags: Set<QueryFlag> = defaultFlags,
    val contexts: List<Context> = emptyList()
) {
    companion object {
        private val defaultFlags = setOf(
            QueryFlag.RESOLVE_INHERITANCE,
            QueryFlag.INCLUDE_NODES_WITHOUT_SERVER_CONTEXT,
            QueryFlag.INCLUDE_NODES_WITHOUT_WORLD_CONTEXT,
            QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT,
            QueryFlag.APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT
        )
    }
} 