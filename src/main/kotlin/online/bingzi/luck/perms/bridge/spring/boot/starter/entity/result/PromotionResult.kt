package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 晋升结果类
 *
 * 该类用于表示晋升操作的结果，包含了操作是否成功的状态以及相关的组信息。
 * 在整个权限管理系统中，该类主要用于反馈晋升操作的执行结果，方便调用者进行后续处理。
 *
 * @property success 是否成功，表示晋升操作是否成功执行。
 * @property status 当前的状态，使用 PromotionStatus 枚举来表示操作的具体状态。
 * @property groupFrom 原组名，表示晋升前所属的组，可以为 null。
 * @property groupTo 目标组名，表示晋升后所属的组，可以为 null。
 */
data class PromotionResult(
    val success: Boolean, // 表示晋升操作是否成功
    val status: PromotionStatus, // 当前操作的状态
    val groupFrom: String? = null, // 晋升前的组名，可能为空
    val groupTo: String? = null // 晋升后的组名，可能为空
)