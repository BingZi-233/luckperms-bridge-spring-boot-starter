package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Node

/**
 * 用户搜索结果类
 *
 * 该类用于封装用户搜索权限节点的结果信息。它包含用户的唯一标识符和与之匹配的权限节点列表。
 *
 * @property uniqueId 用户的唯一标识符（UUID），用于区分不同用户。
 * @property results 与用户匹配的权限节点列表，包含多个 Node 对象。
 */
data class UserSearchResult(
    /** 用户的唯一标识符，类型为 String */
    val uniqueId: String,

    /** 匹配的权限节点列表，类型为 List<Node>，包含多个权限节点信息 */
    val results: List<Node>
) 