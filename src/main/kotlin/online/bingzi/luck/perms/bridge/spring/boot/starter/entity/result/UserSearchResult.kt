package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Node

/**
 * 用户搜索结果
 *
 * @property uniqueId 用户UUID
 * @property results 匹配的权限节点列表
 */
data class UserSearchResult(
    val uniqueId: String,
    val results: List<Node>
) 