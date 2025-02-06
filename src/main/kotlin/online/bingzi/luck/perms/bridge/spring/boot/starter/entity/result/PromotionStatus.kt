package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result

/**
 * 枚举类：晋升状态
 * 
 * 此枚举类定义了与晋升过程相关的各种状态。它主要用于标识
 * 在执行晋升操作时可能出现的不同结果状态。这些状态可以帮助
 * 系统或用户了解晋升过程的进展和结果。
 */
enum class PromotionStatus {
    /** 
     * 状态：成功
     * 表示晋升操作已成功完成，达成预期目标。
     */
    SUCCESS,

    /** 
     * 状态：添加到第一个组
     * 表示对象已成功添加到第一个组中，通常是晋升过程的一部分。
     */
    ADDED_TO_FIRST_GROUP,

    /** 
     * 状态：轨道格式错误
     * 表示输入的轨道格式不符合预期，导致无法正确处理晋升。
     */
    MALFORMED_TRACK,

    /** 
     * 状态：已到达轨道末端
     * 表示晋升操作已达到轨道的末端，可能无法继续进一步的晋升。
     */
    END_OF_TRACK,

    /** 
     * 状态：模糊调用
     * 表示在调用晋升操作时出现了模糊的情况，可能导致不明确的结果。
     */
    AMBIGUOUS_CALL,

    /** 
     * 状态：未定义的失败
     * 表示晋升操作未能成功完成，并且失败的原因未在其他状态中定义。
     */
    UNDEFINED_FAILURE
}