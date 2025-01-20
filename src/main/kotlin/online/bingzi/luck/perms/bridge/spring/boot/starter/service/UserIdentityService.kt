package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 用户身份服务接口
 * 用于获取当前请求的用户身份信息
 */
interface UserIdentityService {
    /**
     * 获取当前用户ID
     * @return 用户UUID
     */
    fun getCurrentUserId(): UUID
} 