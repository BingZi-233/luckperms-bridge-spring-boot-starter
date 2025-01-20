package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Context

/**
 * 查询选项
 *
 * @property mode 查询模式
 * @property flags 查询标志
 * @property contexts 上下文集合
 */
data class QueryOptions(
    val mode: QueryMode = QueryMode.CONTEXTUAL,
    val flags: List<QueryFlag> = defaultQueryFlags,
    val contexts: List<Context> = emptyList()
) 