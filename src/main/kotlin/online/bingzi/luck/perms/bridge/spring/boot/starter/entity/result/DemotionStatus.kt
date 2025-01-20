package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 降级状态
 */
enum class DemotionStatus {
    /** 成功 */
    SUCCESS,
    /** 从第一个组移除 */
    REMOVED_FROM_FIRST_GROUP,
    /** 轨道格式错误 */
    MALFORMED_TRACK,
    /** 不在轨道上 */
    NOT_ON_TRACK,
    /** 模糊调用 */
    AMBIGUOUS_CALL,
    /** 未定义的失败 */
    UNDEFINED_FAILURE
} 