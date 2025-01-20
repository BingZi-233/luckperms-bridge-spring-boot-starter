package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 权限服务接口
 * 定义权限检查的抽象方法
 */
interface PermissionService {
    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户UUID
     * @param permission 权限节点
     * @return 是否拥有权限
     */
    fun hasPermission(userId: UUID, permission: String): Boolean
    
    /**
     * 检查用户是否拥有指定权限列表中的任意一个权限
     * @param userId 用户UUID
     * @param permissions 权限节点列表
     * @return 是否拥有权限
     */
    fun hasAnyPermission(userId: UUID, permissions: Array<String>): Boolean {
        return permissions.any { hasPermission(userId, it) }
    }
    
    /**
     * 检查用户是否拥有指定权限列表中的所有权限
     * @param userId 用户UUID
     * @param permissions 权限节点列表
     * @return 是否拥有权限
     */
    fun hasAllPermissions(userId: UUID, permissions: Array<String>): Boolean {
        return permissions.all { hasPermission(userId, it) }
    }
} 