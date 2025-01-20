package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.UUID

/**
 * 基于请求头的用户身份服务实现
 */
@Service
class HeaderUserIdentityService : UserIdentityService {
    
    override fun getCurrentUserId(): UUID {
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
            ?: throw IllegalStateException("无法获取当前请求上下文")
            
        return request.getHeader("X-User-UUID")?.let { UUID.fromString(it) }
            ?: throw IllegalStateException("未找到用户UUID")
    }
} 