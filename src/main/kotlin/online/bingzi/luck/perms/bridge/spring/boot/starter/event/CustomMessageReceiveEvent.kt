package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 自定义消息接收事件
 * 当LuckPerms接收到通过消息服务发送的自定义消息时触发此事件
 * 
 * 此事件用于：
 * 1. 实现插件间的自定义通信
 * 2. 处理特定的业务逻辑消息
 * 3. 在不同服务器节点间传递自定义数据
 *
 * @property message 自定义消息内容
 *                  消息格式由发送方定义，接收方需要按照约定解析
 *                  建议使用JSON格式以便于处理
 */
data class CustomMessageReceiveEvent(
    @JsonProperty("message")
    val message: String
) : LuckPermsEvent {
    override fun getType(): String = "custom-message-receive"
} 