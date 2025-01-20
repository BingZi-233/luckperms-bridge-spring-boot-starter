package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Node

/**
 * 权限检查结果
 *
 * @property result 检查结果("true", "false", "undefined")
 * @property node 匹配的权限节点(如果有)
 */
data class PermissionCheckResult @JsonCreator constructor(
    @JsonProperty("result") val result: String,
    @JsonProperty("node") val node: Node? = null
) 