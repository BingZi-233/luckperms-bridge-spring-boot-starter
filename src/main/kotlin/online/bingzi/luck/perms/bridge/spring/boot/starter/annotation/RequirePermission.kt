package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode

/**
 * 权限要求注解
 * 该注解用于标注需要进行权限检查的方法或类。开发者可以使用此注解来明确标识哪些操作需要特定的权限，从而实现细粒度的权限管理。
 *
 * @property value 需要检查的权限节点列表，表示哪些权限是该方法或类所需要的。
 * @property mode 权限检查模式，默认为ANY，表示只要满足任一权限即可通过检查。
 * @property message 权限不足时的提示消息，默认提示为“权限不足”。
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS) // 该注解可以应用于方法和类
@Retention(AnnotationRetention.RUNTIME) // 注解在运行时可被访问
@MustBeDocumented // 注解将包含在文档中
annotation class RequirePermission(
    /**
     * 需要检查的权限节点列表
     * 表示该方法或类所需的权限，开发者可以传入一个或多个权限字符串。
     */
    vararg val value: String,

    /**
     * 权限检查模式
     * 该参数指定权限检查的方式，类型为CheckMode，开发者可以选择不同的模式来定义权限的检查逻辑。
     * @see CheckMode 包含可能的权限检查模式。
     */
    val mode: CheckMode = CheckMode.ANY,

    /**
     * 权限不足时的提示消息
     * 当用户的权限不足以访问被标注的资源时，将返回该消息以提示用户。
     */
    val message: String = "权限不足"
)