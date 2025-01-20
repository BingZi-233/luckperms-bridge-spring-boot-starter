package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

import jakarta.validation.constraints.NotEmpty

/**
 * 操作记录实体类
 *
 * 用于记录LuckPerms中的权限变更操作。每个操作记录包含了操作的时间、
 * 执行者、操作对象以及具体的操作描述，用于审计和追踪权限变更历史。
 *
 * @property timestamp 操作时间戳（Unix时间戳，单位：秒）
 * @property source 操作来源，记录执行操作的实体信息
 * @property target 操作目标，记录被操作的实体信息
 * @property description 操作描述，详细说明具体的操作内容
 */
data class Action(
    val timestamp: Long = System.currentTimeMillis() / 1000,
    
    val source: ActionSource,
    
    val target: ActionTarget,
    
    @field:NotEmpty
    val description: String
)