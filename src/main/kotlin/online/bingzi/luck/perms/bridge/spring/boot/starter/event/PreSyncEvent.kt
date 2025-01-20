package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 同步前事件
 * 在LuckPerms开始进行本地数据同步之前触发此事件
 * 本地同步通常发生在：
 * 1. 服务器启动时
 * 2. 手动执行同步命令时
 * 3. 配置的自动同步间隔到达时
 *
 * 此事件主要用于：
 * 1. 通知插件即将开始同步操作
 * 2. 允许其他插件在同步开始前执行必要的准备工作
 * 3. 提供同步操作的追踪点
 */
data class PreSyncEvent(
    @JsonProperty("type")
    private val type: String = "pre-sync"
) : LuckPermsEvent {
    override fun getType(): String = type
} 