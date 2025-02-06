package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 用户查找结果类
 *
 * 该类用于封装用户查找操作的结果信息，主要包含用户的唯一标识和用户名。
 * 在系统中，当需要返回用户信息时，可以使用此类来传递相关数据。
 *
 * @property uniqueId 用户的唯一标识符（UUID），用于唯一标识一个用户。
 * @property username 用户的名称，用于展示或识别用户。
 */
data class UserLookupResult(
    /** 用户的唯一标识符（UUID）。 */
    val uniqueId: String,
    
    /** 用户的名称。 */
    val username: String
) 