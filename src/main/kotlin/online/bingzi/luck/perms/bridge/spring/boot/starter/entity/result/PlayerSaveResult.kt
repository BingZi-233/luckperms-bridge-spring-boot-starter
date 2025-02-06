package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 玩家数据保存结果类
 *
 * 该类用于封装玩家数据保存操作的结果，包括保存操作的结果列表、之前的用户名和其他使用相同用户名的UUID列表。
 *
 * @property outcomes 保存操作的结果列表，包含多个 PlayerSaveResultOutcome 对象，描述每个保存操作的结果。
 * @property previousUsername 之前的用户名，仅在结果为 username_updated 时有效，表示更新前的用户名。
 * @property otherUniqueIds 其他使用相同用户名的UUID列表，仅在结果为 other_unique_ids_present_for_username 时有效，包含与当前用户名相同的其他玩家的UUID。
 */
data class PlayerSaveResult(
    val outcomes: List<PlayerSaveResultOutcome>, // 用于保存操作的结果，类型为 PlayerSaveResultOutcome 的列表
    val previousUsername: String? = null, // 表示更新前的用户名，类型为可空字符串
    val otherUniqueIds: List<String>? = null // 表示与当前用户名相同的其他玩家UUID，类型为可空的字符串列表
)