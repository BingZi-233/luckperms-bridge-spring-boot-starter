package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Node

/**
 * 权限检查结果类
 *
 * 该类用于表示权限检查的结果，包括检查的状态和与之相关的权限节点信息。
 * 它在权限管理系统中扮演着重要角色，用于向调用者返回权限验证的结果。
 *
 * @property result 检查结果，可能的值有 "true" 表示权限通过，"false" 表示权限未通过，
 * "undefined" 表示权限状态未定义。
 * @property node 匹配的权限节点，如果有权限节点匹配，则该属性会返回相应的节点信息。
 */
data class PermissionCheckResult @JsonCreator constructor(
    /**
     * 权限检查的结果状态
     *
     * @param result 权限检查结果字符串，取值为 "true", "false" 或 "undefined"。
     */
    @JsonProperty("result") val result: String,

    /**
     * 匹配的权限节点
     *
     * @param node 可选的权限节点对象，如果存在与该检查结果匹配的权限节点，则返回该节点。
     * 如果没有匹配的节点，则该值为 null。
     */
    @JsonProperty("node") val node: Node? = null
)