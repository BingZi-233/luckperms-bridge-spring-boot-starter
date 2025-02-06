package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.api.UserApi
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.PermissionCheckRequest
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.QueryOptions
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.PermissionService
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * LuckPerms权限服务实现类
 * 该类实现了PermissionService接口，主要用于检查用户的权限。
 * 通过调用UserApi来与权限系统进行交互，以获取用户的权限状态。
 */
@Service
class LuckPermsPermissionService(
    private val userApi: UserApi // 用户API，用于与权限系统进行交互
) : PermissionService {

    /**
     * 检查用户是否具有指定的权限
     * 
     * @param userId 用户的唯一标识符，类型为UUID
     * @param permission 要检查的权限字符串
     * @return 返回一个布尔值，表示用户是否具有该权限
     */
    override fun hasPermission(userId: UUID, permission: String): Boolean {
        // 创建权限检查请求对象，包括权限和查询选项
        val request = PermissionCheckRequest(
            permission = permission, // 权限字符串
            queryOptions = QueryOptions() // 查询选项，当前为空
        )
        // 调用用户API检查用户权限，并解析结果
        return userApi.checkUserPermissionWithOptions(userId.toString(), request)
            .execute() // 执行请求
            .body() // 获取响应体
            ?.result == "true" // 检查结果是否为"true"
    }

    /**
     * 检查用户是否具有所有指定的权限
     * 
     * @param userId 用户的唯一标识符，类型为UUID
     * @param permissions 要检查的权限字符串数组
     * @return 返回一个布尔值，表示用户是否具有所有指定的权限
     */
    override fun hasAllPermissions(userId: UUID, permissions: Array<String>): Boolean {
        // 使用all函数检查所有权限是否都满足条件
        return permissions.all { permission ->
            // 创建权限检查请求对象
            val request = PermissionCheckRequest(
                permission = permission, // 当前要检查的权限
                queryOptions = QueryOptions() // 查询选项，当前为空
            )
            // 调用用户API检查用户权限，并解析结果
            userApi.checkUserPermissionWithOptions(userId.toString(), request)
                .execute() // 执行请求
                .body() // 获取响应体
                ?.result == "true" // 检查结果是否为"true"
        }
    }

    /**
     * 检查用户是否具有任意一个指定的权限
     * 
     * @param userId 用户的唯一标识符，类型为UUID
     * @param permissions 要检查的权限字符串数组
     * @return 返回一个布尔值，表示用户是否具有任何一个指定的权限
     */
    override fun hasAnyPermission(userId: UUID, permissions: Array<String>): Boolean {
        // 使用any函数检查是否有任意权限满足条件
        return permissions.any { permission ->
            // 创建权限检查请求对象
            val request = PermissionCheckRequest(
                permission = permission, // 当前要检查的权限
                queryOptions = QueryOptions() // 查询选项，当前为空
            )
            // 调用用户API检查用户权限，并解析结果
            userApi.checkUserPermissionWithOptions(userId.toString(), request)
                .execute() // 执行请求
                .body() // 获取响应体
                ?.result == "true" // 检查结果是否为"true"
        }
    }
}