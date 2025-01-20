package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 组服务接口
 * 定义组检查的抽象方法
 */
interface GroupService {
    /**
     * 检查用户是否属于指定组
     * @param userId 用户UUID
     * @param groupName 组名
     * @return 是否属于该组
     */
    fun isInGroup(userId: UUID, groupName: String): Boolean
    
    /**
     * 检查用户是否属于指定组列表中的任意一个组
     * @param userId 用户UUID
     * @param groupNames 组名列表
     * @return 是否属于任意一个组
     */
    fun isInAnyGroup(userId: UUID, groupNames: Array<String>): Boolean {
        return groupNames.any { isInGroup(userId, it) }
    }
    
    /**
     * 检查用户是否属于指定组列表中的所有组
     * @param userId 用户UUID
     * @param groupNames 组名列表
     * @return 是否属于所有组
     */
    fun isInAllGroups(userId: UUID, groupNames: Array<String>): Boolean {
        return groupNames.all { isInGroup(userId, it) }
    }
} 