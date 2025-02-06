package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 自定义消息请求实体类
 *
 * 该类用于表示一个自定义的消息请求，包含消息的通道ID和消息内容。 
 * 在消息传递系统中，可以通过该实体来封装消息的相关信息，便于在不同组件之间进行传递。
 *
 * @property channelId 消息通道ID，表示消息发送的目标通道，类型为 String。
 * @property payload 消息内容，表示待发送的具体消息内容，类型为 String。
 */
data class CustomMessage(
    val channelId: String,  // 消息通道的唯一标识符，用于指定消息的发送目标
    val payload: String      // 消息的具体内容，包含要传递的信息
) 