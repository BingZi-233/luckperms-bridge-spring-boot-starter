package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.NodeType
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.GroupService
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * LuckPerms组服务实现类
 */
@Service
class LuckPermsGroupService(
    private val userApi: UserApi
) : GroupService {
    
    override fun isInGroup(userId: UUID, groupName: String): Boolean {
        return userApi.getUser(userId.toString()).execute().body()?.nodes
            ?.any { node -> 
                node.type == NodeType.INHERITANCE && node.key == "group.$groupName" && node.value 
            } ?: false
    }
} 