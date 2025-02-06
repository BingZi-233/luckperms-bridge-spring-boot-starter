package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Node

/**
 * 组搜索结果类
 *
 * 此类用于表示组的搜索结果，包含组的名称以及与之相关的搜索结果节点列表。
 * 主要功能是提供一个结构化的方式来存储和传递组的搜索结果数据。
 *
 * @property name 组名称，表示该组的标识或称呼。
 * @property results 搜索结果节点列表，包含与该组相关的所有节点信息。
 */
data class GroupSearchResult(
    /** 组名称 */
    val name: String,
    
    /** 搜索结果节点列表 */
    val results: List<Node>
) 