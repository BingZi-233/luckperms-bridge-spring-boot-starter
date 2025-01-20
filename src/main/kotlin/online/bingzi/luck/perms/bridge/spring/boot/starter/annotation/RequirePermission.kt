package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode

/**
 * 权限要求注解
 * 用于标注需要进行权限检查的方法或类
 *
 * @property value 需要检查的权限节点列表
 * @property mode 权限检查模式,默认为ANY(任一权限满足即可)
 * @property message 权限不足时的提示消息
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequirePermission(
    /**
     * 需要检查的权限节点列表
     */
    vararg val value: String,

    /**
     * 权限检查模式
     * @see CheckMode
     */
    val mode: CheckMode = CheckMode.ANY,

    /**
     * 权限不足时的提示消息
     */
    val message: String = "权限不足"
)