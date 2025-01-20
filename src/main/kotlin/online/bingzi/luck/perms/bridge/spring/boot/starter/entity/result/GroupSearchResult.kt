package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Node

/**
 * 组搜索结果
 *
 * @property name 组名称
 * @property results 搜索结果节点列表
 */
data class GroupSearchResult(
    val name: String,
    val results: List<Node>
) 