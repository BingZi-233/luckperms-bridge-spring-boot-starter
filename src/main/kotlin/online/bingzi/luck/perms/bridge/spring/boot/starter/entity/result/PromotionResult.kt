package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 晋升结果
 *
 * @property success 是否成功
 * @property status 状态
 * @property groupFrom 原组名
 * @property groupTo 目标组名
 */
data class PromotionResult(
    val success: Boolean,
    val status: PromotionStatus,
    val groupFrom: String? = null,
    val groupTo: String? = null
)