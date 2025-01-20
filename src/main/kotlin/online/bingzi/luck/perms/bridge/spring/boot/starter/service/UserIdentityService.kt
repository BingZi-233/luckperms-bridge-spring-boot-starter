package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 用户身份服务接口
 * 定义获取当前用户信息的抽象方法
 */
interface UserIdentityService {
    /**
     * 获取当前用户UUID
     * @return 用户UUID
     * @throws IllegalStateException 如果无法获取用户信息
     */
    fun getCurrentUserId(): UUID
} 