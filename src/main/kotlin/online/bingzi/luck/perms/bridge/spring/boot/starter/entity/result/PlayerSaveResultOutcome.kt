package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 这个枚举类表示玩家保存操作的结果类型。
 * 主要用于标识在玩家数据保存过程中可能出现的不同结果状态。
 * 在整个系统中，这个枚举类型可以帮助开发者处理不同的保存结果，
 * 例如在进行插入、更新操作时的反馈信息。
 */
enum class PlayerSaveResultOutcome {
    /** 
     * 表示执行了干净的插入操作，即新的玩家数据成功插入数据库中。
     */
    CLEAN_INSERT,

    /** 
     * 表示保存操作没有任何变化，玩家数据保持不变。
     */
    NO_CHANGE,

    /** 
     * 表示玩家的用户名已经被更新至新的值。
     */
    USERNAME_UPDATED,

    /** 
     * 表示存在其他 UUID 使用了相同的用户名，导致当前保存操作无法完成。
     */
    OTHER_UNIQUE_IDS_PRESENT_FOR_USERNAME
}