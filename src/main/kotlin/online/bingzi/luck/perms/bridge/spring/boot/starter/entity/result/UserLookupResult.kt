package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 用户查找结果
 *
 * @property uniqueId 用户UUID
 * @property username 用户名
 */
data class UserLookupResult(
    val uniqueId: String,
    val username: String
) 