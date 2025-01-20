package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Context
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.PermissionCheckRequest
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.QueryOptions
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
        val request = PermissionCheckRequest(
            permission = permission,
            queryOptions = QueryOptions(
                contexts = listOf(Context(key = contextKey, value = contextValue))
            )
        )
        return userApi.checkUserPermissionWithOptions(userId.toString(), request)
            .execute()
            .body()
            ?.result == "true"
    }
} 