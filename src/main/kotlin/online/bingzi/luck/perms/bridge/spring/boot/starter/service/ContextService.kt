package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 上下文服务接口
 * 该接口定义了用于检查用户在特定上下文下是否具有特定权限的方法。
 * 主要用于权限管理系统中，提供用户权限的上下文检查功能。
 */
interface ContextService {
    
    /**
     * 检查用户在指定上下文下是否有权限
     * @param userId 用户UUID，用于唯一标识用户
     * @param permission 权限节点，表示需要检查的具体权限
     * @param contextKey 上下文键，表示权限检查所依赖的上下文的键
     * @param contextValue 上下文值，表示权限检查所依赖的上下文的值
     * @return 如果用户在该上下文下具有指定权限，则返回true；否则返回false
     */
    fun hasPermissionInContext(
        userId: UUID,
        permission: String,
        contextKey: String,
        contextValue: String
    ): Boolean
    
    /**
     * 检查用户在任意指定上下文下是否有权限
     * @param userId 用户UUID，用于唯一标识用户
     * @param permission 权限节点，表示需要检查的具体权限
     * @param contexts 上下文键值对列表，包含多个上下文的键和值
     * @return 如果用户在任意上下文中具有指定权限，则返回true；否则返回false
     */
    fun hasPermissionInAnyContext(
        userId: UUID,
        permission: String,
        contexts: Map<String, String>
    ): Boolean {
        // 遍历上下文键值对，检查用户在每个上下文下的权限
        return contexts.any { (key, value) ->
            hasPermissionInContext(userId, permission, key, value)
        }
    }
    
    /**
     * 检查用户在所有指定上下文下是否有权限
     * @param userId 用户UUID，用于唯一标识用户
     * @param permission 权限节点，表示需要检查的具体权限
     * @param contexts 上下文键值对列表，包含多个上下文的键和值
     * @return 如果用户在所有上下文中均具有指定权限，则返回true；否则返回false
     */
    fun hasPermissionInAllContexts(
        userId: UUID,
        permission: String,
        contexts: Map<String, String>
    ): Boolean {
        // 遍历上下文键值对，检查用户在每个上下文下的权限
        return contexts.all { (key, value) ->
            hasPermissionInContext(userId, permission, key, value)
        }
    }
} 