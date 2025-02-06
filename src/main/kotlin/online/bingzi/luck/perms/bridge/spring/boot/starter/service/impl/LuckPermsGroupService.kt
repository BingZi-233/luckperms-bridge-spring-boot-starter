package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.NodeType
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.GroupService
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * LuckPerms组服务实现类
 * 
 * 该类用于实现组相关的服务，主要功能是判断用户是否属于指定的组。
 * 它依赖于UserApi来获取用户的相关信息，并检查用户的节点信息以确定其组归属。
 */
@Service
class LuckPermsGroupService(
    private val userApi: UserApi // 用户API，用于获取用户信息
) : GroupService {

    /**
     * 检查用户是否属于指定的组
     *
     * @param userId 用户的唯一标识符，类型为UUID，表示要检查的用户。
     * @param groupName 要检查的组名，类型为String，表示用户是否属于该组。
     * @return Boolean 返回值，若用户属于指定组则为true，否则为false。
     * 可能返回null，如果获取用户信息失败或用户不存在。
     */
    override fun isInGroup(userId: UUID, groupName: String): Boolean {
        // 调用UserApi获取指定用户的信息并检查其节点
        return userApi.getUser(userId.toString()).execute().body()?.nodes
            ?.any { node -> 
                // 检查节点是否为继承类型且键与组名匹配
                node.type == NodeType.INHERITANCE && node.key == "group.$groupName" && node.value 
            } ?: false // 如果节点列表为空，返回false
    }
} 