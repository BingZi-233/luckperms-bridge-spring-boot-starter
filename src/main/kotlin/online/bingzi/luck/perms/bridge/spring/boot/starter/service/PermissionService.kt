package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 权限服务接口
 * 该接口定义了与权限相关的抽象方法，主要用于检查用户的权限。
 * 实现该接口的类需要提供具体的权限检查逻辑。
 */
interface PermissionService {
    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户UUID，表示需要检查权限的用户的唯一标识符
     * @param permission 权限节点，表示需要检查的具体权限
     * @return Boolean，返回用户是否拥有指定权限，true 表示拥有，false 表示不拥有
     */
    fun hasPermission(userId: UUID, permission: String): Boolean
    
    /**
     * 检查用户是否拥有指定权限列表中的任意一个权限
     * @param userId 用户UUID，表示需要检查权限的用户的唯一标识符
     * @param permissions 权限节点列表，表示需要检查的多个权限
     * @return Boolean，返回用户是否拥有权限列表中的任意一个权限，true 表示拥有至少一个权限，false 表示没有任何权限
     */
    fun hasAnyPermission(userId: UUID, permissions: Array<String>): Boolean {
        // 使用 any 函数遍历权限列表，检查用户是否拥有其中的任意一个权限
        return permissions.any { hasPermission(userId, it) }
    }
    
    /**
     * 检查用户是否拥有指定权限列表中的所有权限
     * @param userId 用户UUID，表示需要检查权限的用户的唯一标识符
     * @param permissions 权限节点列表，表示需要检查的多个权限
     * @return Boolean，返回用户是否拥有所有指定权限，true 表示拥有所有权限，false 表示至少缺少一个权限
     */
    fun hasAllPermissions(userId: UUID, permissions: Array<String>): Boolean {
        // 使用 all 函数遍历权限列表，检查用户是否拥有所有的权限
        return permissions.all { hasPermission(userId, it) }
    }
}