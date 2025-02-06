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
 * 
 * 此类实现了ContextService接口，负责处理与用户权限相关的上下文信息。
 * 主要功能包括检查用户在特定上下文下是否具有某个权限。
 */
@Service
class LuckPermsContextService(
    private val userApi: UserApi // 用户API，用于与用户权限信息进行交互
) : ContextService {

    /**
     * 检查用户在特定上下文下是否具有指定权限
     * 
     * @param userId 用户的唯一标识符，UUID类型
     * @param permission 要检查的权限字符串
     * @param contextKey 上下文的键，用于标识上下文的类型
     * @param contextValue 上下文的值，具体的上下文信息
     * @return 返回Boolean值，表示用户是否在指定上下文下具有该权限
     * 
     * 可能抛出的异常：
     * - 可能会抛出网络请求相关的异常，例如连接失败等
     */
    override fun hasPermissionInContext(
        userId: UUID,
        permission: String,
        contextKey: String,
        contextValue: String
    ): Boolean {
        // 创建权限检查请求对象，包含要检查的权限和查询选项
        val request = PermissionCheckRequest(
            permission = permission, // 权限字符串
            queryOptions = QueryOptions(
                contexts = listOf(Context(key = contextKey, value = contextValue)) // 上下文列表，只有一个上下文
            )
        )
        
        // 调用用户API来检查用户的权限，并返回结果
        return userApi.checkUserPermissionWithOptions(userId.toString(), request) // 将UUID转换为字符串
            .execute() // 执行请求
            .body() // 获取响应体
            ?.result == "true" // 检查结果是否为"true"，如果是则返回true，否则返回false
    }
}