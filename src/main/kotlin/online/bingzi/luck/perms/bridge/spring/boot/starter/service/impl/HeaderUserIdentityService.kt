package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.UUID

/**
 * 基于请求头的用户身份服务实现类
 * 
 * 该类实现了 UserIdentityService 接口，通过解析请求头中的用户 UUID 
 * 来获取当前用户的身份信息。主要用于在 Spring Boot 应用中提供用户身份识别的功能。
 */
@Service
class HeaderUserIdentityService : UserIdentityService {
    
    /**
     * 获取当前用户的 UUID。
     * 
     * 该方法从当前 HTTP 请求的头部获取 "X-User-UUID" 字段，并将其解析为 UUID 对象。
     * 
     * @return 当前用户的 UUID，类型为 UUID。
     * @throws IllegalStateException 如果无法获取当前请求上下文或未找到用户 UUID。
     */
    override fun getCurrentUserId(): UUID {
        // 获取当前请求的 ServletRequestAttributes 对象
        // 如果请求上下文不存在，抛出异常
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
            ?: throw IllegalStateException("无法获取当前请求上下文")
            
        // 从请求头中获取 "X-User-UUID" 的值，并尝试将其转换为 UUID
        // 如果未找到该头部或者转换失败，抛出异常
        return request.getHeader("X-User-UUID")?.let { UUID.fromString(it) }
            ?: throw IllegalStateException("未找到用户UUID")
    }
}