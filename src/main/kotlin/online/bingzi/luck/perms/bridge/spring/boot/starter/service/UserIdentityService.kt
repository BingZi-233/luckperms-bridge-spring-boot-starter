package online.bingzi.luck.perms.bridge.spring.boot.starter.service

import java.util.UUID

/**
 * 用户身份服务接口
 * 该接口定义了获取当前用户身份信息的方法，主要用于在应用程序中对用户进行身份验证和授权。
 */
interface UserIdentityService {
    /**
     * 获取当前用户ID
     * 此方法用于返回当前请求的用户的唯一标识符（UUID），
     * 该UUID可用于在系统中识别和操作对应的用户。
     * 
     * @return 用户UUID，表示当前用户的唯一身份标识。
     */
    fun getCurrentUserId(): UUID
}