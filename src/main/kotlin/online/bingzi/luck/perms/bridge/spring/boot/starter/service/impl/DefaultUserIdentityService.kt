package online.bingzi.luck.perms.bridge.spring.boot.starter.service.impl

import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 默认用户身份服务实现类
 * 仅用于开发和测试环境，生产环境应该使用自定义实现
 */
@Service
@Primary
class DefaultUserIdentityService : UserIdentityService {
    override fun getCurrentUserId(): UUID {
        // 返回一个固定的测试用户ID
        return UUID.fromString("00000000-0000-0000-0000-000000000000")
    }
} 