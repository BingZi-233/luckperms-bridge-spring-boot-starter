package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

/**
 * 操作目标类型枚举
 *
 * 该枚举定义了系统中可能的操作目标类型，主要用于标识不同的操作对象。
 * - USER: 代表一个用户，通常指代系统中的玩家。
 * - GROUP: 代表一个权限组，用于对一组用户施加相同的操作。
 * - TRACK: 代表一个权限轨道，通常用于定义权限的执行顺序或结构。
 */
enum class ActionTargetType {
    USER,  // 表示操作目标是一个用户
    GROUP, // 表示操作目标是一个权限组
    TRACK  // 表示操作目标是一个权限轨道
}