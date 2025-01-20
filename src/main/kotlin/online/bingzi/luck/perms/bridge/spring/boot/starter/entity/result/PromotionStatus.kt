package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 晋升状态
 */
enum class PromotionStatus {
    /** 成功 */
    SUCCESS,
    /** 添加到第一个组 */
    ADDED_TO_FIRST_GROUP,
    /** 轨道格式错误 */
    MALFORMED_TRACK,
    /** 已到达轨道末端 */
    END_OF_TRACK,
    /** 模糊调用 */
    AMBIGUOUS_CALL,
    /** 未定义的失败 */
    UNDEFINED_FAILURE
} 