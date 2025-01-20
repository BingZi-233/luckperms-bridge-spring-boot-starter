package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 同步后事件
 * 在LuckPerms完成本地数据同步后触发此事件
 * 本地同步完成后会触发此事件，表示：
 * 1. 所有本地数据已经与存储后端同步完成
 * 2. 内存中的权限数据已经更新
 * 3. 缓存已经刷新
 *
 * 此事件主要用于：
 * 1. 通知插件同步操作已完成
 * 2. 允许其他插件在同步完成后更新其状态
 * 3. 进行同步后的清理工作
 * 4. 重新加载依赖于权限数据的功能
 */
data class PostSyncEvent(
    @JsonProperty("type")
    private val type: String = "post-sync"
) : LuckPermsEvent {
    override fun getType(): String = type
} 