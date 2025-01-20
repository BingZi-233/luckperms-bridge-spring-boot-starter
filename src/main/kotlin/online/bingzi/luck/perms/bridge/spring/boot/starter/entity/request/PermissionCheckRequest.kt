package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 权限检查请求
 *
 * @property permission 需要检查的权限
 * @property queryOptions 查询选项
 */
data class PermissionCheckRequest(
    val permission: String,
    val queryOptions: QueryOptions
)