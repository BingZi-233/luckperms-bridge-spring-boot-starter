package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 组服务接口
 * 该接口定义了用于检查用户与组之间关系的抽象方法。
 * 主要用于权限管理，确保用户在特定组中的归属关系。
 */
interface GroupService {
    /**
     * 检查用户是否属于指定组
     * @param userId 用户的唯一标识符，类型为UUID
     * @param groupName 要检查的组名，类型为String
     * @return 返回一个Boolean值，表示用户是否属于指定的组
     */
    fun isInGroup(userId: UUID, groupName: String): Boolean
    
    /**
     * 检查用户是否属于指定组列表中的任意一个组
     * @param userId 用户的唯一标识符，类型为UUID
     * @param groupNames 要检查的组名列表，类型为Array<String>
     * @return 返回一个Boolean值，表示用户是否属于任意一个指定的组
     * 该方法通过调用isInGroup方法，检查用户是否在groupNames数组中的任意一个组内
     */
    fun isInAnyGroup(userId: UUID, groupNames: Array<String>): Boolean {
        // 使用any函数遍历groupNames数组，检查用户是否在任意一个组内
        return groupNames.any { isInGroup(userId, it) }
    }
    
    /**
     * 检查用户是否属于指定组列表中的所有组
     * @param userId 用户的唯一标识符，类型为UUID
     * @param groupNames 要检查的组名列表，类型为Array<String>
     * @return 返回一个Boolean值，表示用户是否属于所有指定的组
     * 该方法通过调用isInGroup方法，检查用户是否在groupNames数组中的每一个组内
     */
    fun isInAllGroups(userId: UUID, groupNames: Array<String>): Boolean {
        // 使用all函数遍历groupNames数组，检查用户是否在所有组内
        return groupNames.all { isInGroup(userId, it) }
    }
}