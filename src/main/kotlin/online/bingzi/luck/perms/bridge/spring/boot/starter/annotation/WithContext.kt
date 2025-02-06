package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode

/**
 * 上下文要求注解
 * 该注解用于指定在进行权限检查时所需的上下文条件。
 * 可以应用于类或函数，并且允许重复使用。
 *
 * @property key 上下文键，用于标识上下文的名称或类型
 * @property value 上下文值，表示上下文键对应的具体值
 * @property mode 上下文检查模式，决定了如何进行上下文值的匹配，默认为ANY（只需满足任一上下文即可）
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class WithContext(
    /**
     * 上下文键
     * 用于表示权限检查中所需的上下文的名称或标识符。
     */
    val key: String,
    
    /**
     * 上下文值
     * 与上下文键相对应的具体值，用于进行权限检查时的匹配。
     */
    val value: String,
    
    /**
     * 上下文检查模式
     * 确定如何检查上下文值的匹配方式。
     * 默认值为 CheckMode.ANY，表示只需满足任一上下文条件。
     * 可选值参见 CheckMode 类。
     */
    val mode: CheckMode = CheckMode.ANY
)