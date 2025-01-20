package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 玩家数据保存结果
 *
 * @property outcomes 保存操作的结果列表
 * @property previousUsername 之前的用户名(仅在username_updated结果中有效)
 * @property otherUniqueIds 其他使用相同用户名的UUID列表(仅在other_unique_ids_present_for_username结果中有效)
 */
data class PlayerSaveResult(
    val outcomes: List<PlayerSaveResultOutcome>,
    val previousUsername: String? = null,
    val otherUniqueIds: List<String>? = null
)