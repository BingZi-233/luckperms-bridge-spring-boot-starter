package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
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
        return userApi.getUser(userId).execute().body()?.groups?.any { 
            it.name == groupName 
        } ?: false
    }
} 