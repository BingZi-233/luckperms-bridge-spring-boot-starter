package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 表示一个权限检查请求的实体类。
 *
 * 该类用于封装权限检查所需的信息，包括需要检查的权限和查询选项。
 * 在权限管理系统中，该请求通常由客户端发送，服务器根据此请求进行权限验证。
 *
 * @property permission 需要检查的权限，以字符串形式表示，通常为权限的标识符。
 * @property queryOptions 查询选项，包含与权限检查相关的额外信息，如是否需要详细信息等。
 */
data class PermissionCheckRequest(
    /** 
     * 需要检查的权限标识符。
     * 例如，可以是 "READ_USER" 或 "WRITE_POST" 等权限。
     */
    val permission: String,

    /** 
     * 查询选项，包含权限检查时的附加参数。
     * 例如，可以用于指定是否返回额外的权限信息或错误信息。
     */
    val queryOptions: QueryOptions
)