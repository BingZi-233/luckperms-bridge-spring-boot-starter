package online.bingzi.luck.perms.bridge.spring.boot.starter.event.model

import okhttp3.sse.EventSource
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.LuckPermsEvent
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.priority.EventPriority
import online.bingzi.luck.perms.bridge.spring.boot.starter.event.type.EventType

/**
 * PreSyncEvent 类
 * 该类用于处理在同步操作之前的事件。它是 LuckPermsEvent 的一个具体实现，
 * 主要负责在同步操作开始之前进行一些准备工作。
 * 
 * @property source 事件源，表示事件的来源，类型为 EventSource。
 * @property sourceType 字符串类型，表示事件的源类型，用于区分不同的源。
 * @property priority 事件优先级，默认为 NORMAL，确定事件的处理顺序。
 */
class PreSyncEvent(
    source: EventSource, // 事件源，表示触发该事件的源
    val sourceType: String, // 事件源类型，用于标识源的类型
    priority: EventPriority = EventPriority.NORMAL // 事件优先级，默认设置为 NORMAL
) : LuckPermsEvent(source, EventType.PRE_SYNC, priority) // 继承 LuckPermsEvent，设置事件类型为 PRE_SYNC，并传递优先级