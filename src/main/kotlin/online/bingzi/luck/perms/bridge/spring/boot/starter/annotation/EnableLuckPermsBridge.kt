package online.bingzi.luck.perms.bridge.spring.boot.starter.annotation

import online.bingzi.luck.perms.bridge.spring.boot.starter.config.LuckPermsAutoConfiguration
import org.springframework.context.annotation.Import

/**
 * 启用LuckPerms Bridge注解
 * 用于启用LuckPerms Bridge的功能
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
@Import(LuckPermsAutoConfiguration::class)
annotation class EnableLuckPermsBridge 