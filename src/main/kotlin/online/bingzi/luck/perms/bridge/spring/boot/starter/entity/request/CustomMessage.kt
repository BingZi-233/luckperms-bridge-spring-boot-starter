package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 自定义消息请求实体
 *
 * @property channelId 消息通道ID
 * @property payload 消息内容
 */
data class CustomMessage(
    val channelId: String,
    val payload: String
) 