package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.config.LuckPermsAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Import

/**
 * 启用LuckPerms Bridge注解
 * 用于启用LuckPerms Bridge的功能。
 * 
 * 此注解的优先级高于配置文件中的 luck-perms.enabled 属性。
 * 当使用此注解时，将强制启用LuckPerms Bridge，忽略配置文件中的 enabled 设置。
 * 
 * 使用方式:
 * ```kotlin
 * @EnableLuckPermsBridge
 * @SpringBootApplication
 * class YourApplication
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@ConditionalOnProperty(
    prefix = "luck-perms",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Import(LuckPermsAutoConfiguration::class)
annotation class EnableLuckPermsBridge 