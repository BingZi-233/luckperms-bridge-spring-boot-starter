package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.PermissionCheckRequest
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.QueryOptions
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
        val request = PermissionCheckRequest(
            permission = permission,
            queryOptions = QueryOptions()
        )
        return userApi.checkUserPermissionWithOptions(userId.toString(), request)
            .execute()
            .body()
            ?.result == "true"
    }

    override fun hasAllPermissions(userId: UUID, permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            val request = PermissionCheckRequest(
                permission = permission,
                queryOptions = QueryOptions()
            )
            userApi.checkUserPermissionWithOptions(userId.toString(), request)
                .execute()
                .body()
                ?.result == "true"
        }
    }

    override fun hasAnyPermission(userId: UUID, permissions: Array<String>): Boolean {
        return permissions.any { permission ->
            val request = PermissionCheckRequest(
                permission = permission,
                queryOptions = QueryOptions()
            )
            userApi.checkUserPermissionWithOptions(userId.toString(), request)
                .execute()
                .body()
                ?.result == "true"
        }
    }
} 