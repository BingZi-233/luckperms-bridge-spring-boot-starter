package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 日志广播事件
 * 当LuckPerms系统中发生需要记录的操作时触发此事件
 * 例如：权限变更、组变更、用户数据更新等操作
 *
 * @property message 日志消息内容，包含操作的详细信息
 * 消息格式通常为："[操作类型] [操作对象] [操作内容]"
 * 例如："permission set minecraft.command.ban true"
 */
data class LogBroadcastEvent(
    @JsonProperty("message")
    val message: String
) : LuckPermsEvent {
    override fun getType(): String = "log-broadcast"
} 