package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 玩家保存结果类型
 */
enum class PlayerSaveResultOutcome {
    /** 干净的插入操作 */
    CLEAN_INSERT,
    /** 无变化 */
    NO_CHANGE,
    /** 用户名已更新 */
    USERNAME_UPDATED,
    /** 存在使用相同用户名的其他UUID */
    OTHER_UNIQUE_IDS_PRESENT_FOR_USERNAME
} 