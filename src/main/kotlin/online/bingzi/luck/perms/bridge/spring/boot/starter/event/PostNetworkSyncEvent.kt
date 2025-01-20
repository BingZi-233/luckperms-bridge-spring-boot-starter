package online.bingzi.luck.perms.bridge.spring.boot.starter.event

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

/**
 * 网络同步后事件
 * 在LuckPerms完成网络同步操作后触发此事件
 * 用于通知其他节点同步操作已完成，可以进行后续处理
 *
 * @property syncId 同步操作的唯一标识符
 *                 与PreNetworkSyncEvent中的syncId对应
 *                 用于跟踪整个同步过程，确保同步操作的一致性
 * @property type 同步类型，可能的值：
 *              - "full": 完整同步，同步所有数据
 *              - "specific_user": 特定用户同步，仅同步指定用户的数据
 * @property didSyncOccur 同步是否实际发生
 *                      true: 数据发生了实际的更新
 *                      false: 数据未发生变化，无需更新
 * @property specificUserUniqueId 特定用户的UUID
 *                              仅当type为"specific_user"时有效
 *                              指定同步的用户ID
 */
data class PostNetworkSyncEvent(
    @JsonProperty("syncId")
    val syncId: UUID,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("didSyncOccur")
    val didSyncOccur: Boolean,
    @JsonProperty("specificUserUniqueId")
    val specificUserUniqueId: UUID?
) : LuckPermsEvent {
    override fun getType(): String = "post-network-sync"
} 