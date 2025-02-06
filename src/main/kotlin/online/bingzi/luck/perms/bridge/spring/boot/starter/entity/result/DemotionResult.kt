package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 降级结果类，用于表示降级操作的结果状态。
 *
 * 该类包含了降级操作是否成功的信息、当前的状态、以及涉及的原组名和目标组名。
 * 在系统中，降级操作可能用于权限管理或资源控制等场景。
 *
 * @property success 表示降级操作是否成功，true 表示成功，false 表示失败。
 * @property status 表示当前降级操作的状态，使用 DemotionStatus 枚举类型。
 * @property groupFrom 表示降级操作前的原组名，可能为 null，表示没有指定原组。
 * @property groupTo 表示降级操作后的目标组名，可能为 null，表示没有指定目标组。
 */
data class DemotionResult(
    val success: Boolean,  // 降级操作是否成功
    val status: DemotionStatus,  // 当前降级操作的状态
    val groupFrom: String? = null,  // 原组名，可能为 null
    val groupTo: String? = null  // 目标组名，可能为 null
)