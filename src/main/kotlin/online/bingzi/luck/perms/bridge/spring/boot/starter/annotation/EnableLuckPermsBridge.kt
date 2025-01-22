package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.config.LuckPermsAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.context.annotation.Import

/**
 * 启用LuckPerms Bridge注解
 * 用于启用LuckPerms Bridge的功能。
 * 
 * 此注解会强制启用LuckPerms Bridge，完全忽略配置文件中的 luck-perms.enabled 设置。
 * 如果在主类上添加了此注解，则配置文件中的 enabled 属性将不会生效。
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
@ImportAutoConfiguration(LuckPermsAutoConfiguration::class)
annotation class EnableLuckPermsBridge 