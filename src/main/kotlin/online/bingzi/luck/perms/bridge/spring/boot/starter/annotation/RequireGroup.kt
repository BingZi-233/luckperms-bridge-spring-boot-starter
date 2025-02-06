package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode

/**
 * 组要求注解
 * 该注解用于标注需要进行组权限检查的方法或类。
 * 在使用此注解时，可以指定需要检查的组名以及检查模式， 
 * 以确保调用该方法或类的用户具备相应的权限。
 *
 * @property value 需要检查的组名列表。用户必须属于这些组之一才能访问被注解的方法或类。
 * @property mode 组检查模式，默认为ANY，表示只需属于任一指定组即可通过检查。
 * @property message 权限不足时的提示消息，默认提示为"需要指定组权限"。
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS) // 指定注解可以应用于函数或类
@Retention(AnnotationRetention.RUNTIME) // 指定注解在运行时仍可被访问
@MustBeDocumented // 指定注解会被包含在文档中
annotation class RequireGroup(
    /**
     * 需要检查的组名列表。
     * 该参数可以接受多个组名，用户必须属于至少一个指定的组才能访问。
     */
    vararg val value: String,

    /**
     * 组检查模式。
     * 该参数使用CheckMode枚举类型，可以选择不同的检查模式。
     * 默认为CheckMode.ANY，表示只需要满足一个组的权限即可。
     * @see CheckMode 该枚举定义了可用的检查模式。
     */
    val mode: CheckMode = CheckMode.ANY,

    /**
     * 权限不足时的提示消息。
     * 如果用户没有足够的权限访问被注解的方法或类，将返回该消息。
     * 默认消息为"需要指定组权限"。
     */
    val message: String = "需要指定组权限"
)