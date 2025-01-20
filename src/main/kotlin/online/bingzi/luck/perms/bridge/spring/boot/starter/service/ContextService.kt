package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 上下文服务接口
 * 定义上下文检查的抽象方法
 */
interface ContextService {
    /**
     * 检查用户在指定上下文下是否有权限
     * @param userId 用户UUID
     * @param permission 权限节点
     * @param contextKey 上下文键
     * @param contextValue 上下文值
     * @return 是否有权限
     */
    fun hasPermissionInContext(
        userId: UUID,
        permission: String,
        contextKey: String,
        contextValue: String
    ): Boolean
    
    /**
     * 检查用户在任意指定上下文下是否有权限
     * @param userId 用户UUID
     * @param permission 权限节点
     * @param contexts 上下文键值对列表
     * @return 是否有权限
     */
    fun hasPermissionInAnyContext(
        userId: UUID,
        permission: String,
        contexts: Map<String, String>
    ): Boolean {
        return contexts.any { (key, value) ->
            hasPermissionInContext(userId, permission, key, value)
        }
    }
    
    /**
     * 检查用户在所有指定上下文下是否有权限
     * @param userId 用户UUID
     * @param permission 权限节点
     * @param contexts 上下文键值对列表
     * @return 是否有权限
     */
    fun hasPermissionInAllContexts(
        userId: UUID,
        permission: String,
        contexts: Map<String, String>
    ): Boolean {
        return contexts.all { (key, value) ->
            hasPermissionInContext(userId, permission, key, value)
        }
    }
} 