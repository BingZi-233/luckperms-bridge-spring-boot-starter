package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

/**
 * 网络同步前事件
 * 在LuckPerms开始进行网络同步操作之前触发此事件
 * 用于通知其他节点即将开始同步操作，以便做好准备
 *
 * @property syncId 同步操作的唯一标识符
 *                 用于跟踪整个同步过程，确保同步操作的一致性
 * @property type 同步类型，可能的值：
 *              - "full": 完整同步，同步所有数据
 *              - "specific_user": 特定用户同步，仅同步指定用户的数据
 * @property specificUserUniqueId 特定用户的UUID
 *                              仅当type为"specific_user"时有效
 *                              指定要同步的用户ID
 */
data class PreNetworkSyncEvent(
    @JsonProperty("syncId")
    val syncId: UUID,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("specificUserUniqueId")
    val specificUserUniqueId: UUID?
) : LuckPermsEvent {
    override fun getType(): String = "pre-network-sync"
} 