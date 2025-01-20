package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

/**
 * 组要求注解
 * 用于标注需要进行组权限检查的方法或类
 *
 * @property value 需要检查的组名列表
 * @property mode 组检查模式,默认为ANY(属于任一组即可)
 * @property message 权限不足时的提示消息
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequireGroup(
    /**
     * 需要检查的组名列表
     */
    vararg val value: String,
    
    /**
     * 组检查模式
     * @see CheckMode
     */
    val mode: CheckMode = CheckMode.ANY,
    
    /**
     * 权限不足时的提示消息
     */
    val message: String = "需要指定组权限"
) 