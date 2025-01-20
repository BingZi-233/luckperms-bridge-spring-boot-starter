package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

/**
 * 操作目标类型枚举
 *
 * 定义了可能的操作目标类型：
 * - USER: 用户类型，表示操作目标是一个玩家
 * - GROUP: 组类型，表示操作目标是一个权限组
 * - TRACK: 轨道类型，表示操作目标是一个权限轨道
 */
enum class ActionTargetType {
    USER,
    GROUP,
    TRACK
} 