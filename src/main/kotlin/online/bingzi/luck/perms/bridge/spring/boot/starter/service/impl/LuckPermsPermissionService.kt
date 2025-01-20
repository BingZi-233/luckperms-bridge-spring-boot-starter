package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.PermissionService
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * LuckPerms权限服务实现类
 */
@Service
class LuckPermsPermissionService(
    private val userApi: UserApi
) : PermissionService {
    
    override fun hasPermission(userId: UUID, permission: String): Boolean {
        return userApi.checkPermission(userId, permission).execute().body() ?: false
    }
} 