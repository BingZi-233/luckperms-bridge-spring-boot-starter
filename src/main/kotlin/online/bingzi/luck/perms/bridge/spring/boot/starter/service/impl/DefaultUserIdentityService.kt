package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 默认用户身份服务实现类
 * 该类实现了 UserIdentityService 接口，主要用于提供当前用户的身份信息。
 * 该实现仅用于开发和测试环境，生产环境应该使用自定义实现，以保证安全性和灵活性。
 */
@Service
@Primary
class DefaultUserIdentityService : UserIdentityService {
    
    /**
     * 获取当前用户的唯一标识符
     * 该方法返回一个固定的测试用户ID，通常在开发和测试中使用。
     * 
     * @return UUID 当前用户的唯一标识符，固定为 "00000000-0000-0000-0000-000000000000"
     */
    override fun getCurrentUserId(): UUID {
        // 返回一个固定的测试用户ID
        return UUID.fromString("00000000-0000-0000-0000-000000000000")
    }
}