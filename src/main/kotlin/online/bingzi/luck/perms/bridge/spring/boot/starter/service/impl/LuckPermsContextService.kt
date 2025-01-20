package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.ContextService
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * LuckPerms上下文服务实现类
 */
@Service
class LuckPermsContextService(
    private val userApi: UserApi
) : ContextService {
    
    override fun hasPermissionInContext(
        userId: UUID,
        permission: String,
        contextKey: String,
        contextValue: String
    ): Boolean {
        return userApi.checkPermissionInContext(
            userId,
            permission,
            mapOf(contextKey to contextValue)
        ).execute().body() ?: false
    }
} 