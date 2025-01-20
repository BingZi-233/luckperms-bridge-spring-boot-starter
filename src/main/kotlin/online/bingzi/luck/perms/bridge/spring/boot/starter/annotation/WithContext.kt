package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode

/**
 * 上下文要求注解
 * 用于指定权限检查时的上下文条件
 *
 * @property key 上下文键
 * @property value 上下文值
 * @property mode 上下文检查模式,默认为ANY(满足任一上下文即可)
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Repeatable
annotation class WithContext(
    /**
     * 上下文键
     */
    val key: String,
    
    /**
     * 上下文值
     */
    val value: String,
    
    /**
     * 上下文检查模式
     * @see CheckMode
     */
    val mode: CheckMode = CheckMode.ANY
) 