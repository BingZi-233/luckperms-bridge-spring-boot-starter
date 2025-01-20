package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 降级结果
 *
 * @property success 是否成功
 * @property status 状态
 * @property groupFrom 原组名
 * @property groupTo 目标组名
 */
data class DemotionResult(
    val success: Boolean,
    val status: DemotionStatus,
    val groupFrom: String? = null,
    val groupTo: String? = null
)