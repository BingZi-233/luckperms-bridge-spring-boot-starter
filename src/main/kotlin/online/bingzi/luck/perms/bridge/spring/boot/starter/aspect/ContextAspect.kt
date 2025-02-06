package online.bingzi.luck.perms.bridge.spring.boot.starter.aspect

import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.WithContext
import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequirePermission
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode
import online.bingzi.luck.perms.bridge.spring.boot.starter.exception.PermissionDeniedException
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.ContextService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * 上下文切面
 * 此类用于处理带有@WithContext和@RequirePermission注解的方法的上下文检查逻辑。
 * 其作用是确保在进行权限检查之前，先从上下文中获取用户信息。
 */
@Aspect
@Component
@Order(0) // 设置切面的优先级，值越小优先级越高
class ContextAspect(
    private val contextService: ContextService, // 上下文服务，用于检查上下文权限
    private val userIdentityService: UserIdentityService // 用户身份服务，用于获取当前用户信息
) {
    /**
     * 上下文切面处理
     * 拦截带有@WithContext和@RequirePermission注解的方法，并进行上下文权限检查。
     * 
     * @param joinPoint 拦截的连接点，包含方法信息和调用上下文
     * @return Any 方法的返回值，类型取决于被拦截的方法
     * @throws PermissionDeniedException 如果当前用户没有足够的权限
     */
    @Around("@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.WithContext) && " +
            "@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequirePermission)")
    fun checkContext(joinPoint: ProceedingJoinPoint): Any {
        // 获取方法签名，以便获取被调用方法的相关信息
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        
        // 获取方法上的上下文注解
        val contexts = method.getAnnotationsByType(WithContext::class.java)
        // 获取方法上的权限注解
        val permission = method.getAnnotation(RequirePermission::class.java)
        
        // 获取当前用户ID
        val userId = userIdentityService.getCurrentUserId()
        
        // 将上下文注解的信息转换为Map形式，方便后续使用
        val contextMap = contexts.associate { it.key to it.value }
        
        // 根据权限检查模式进行权限验证
        // 这里有两种模式：ANY（只要有一个权限满足）和ALL（所有权限都必须满足）
        val hasPermission = when (permission.mode) {
            CheckMode.ANY -> permission.value.any { perm ->
                // 检查用户在任意一个上下文中是否拥有权限
                contextService.hasPermissionInAnyContext(userId, perm, contextMap)
            }
            CheckMode.ALL -> permission.value.all { perm ->
                // 检查用户在所有上下文中是否拥有权限
                contextService.hasPermissionInAllContexts(userId, perm, contextMap)
            }
        }
        
        // 如果没有权限,则抛出权限拒绝异常
        if (!hasPermission) {
            throw PermissionDeniedException(permission.message) // 使用权限注解中的消息作为异常信息
        }
        
        // 如果拥有权限，则继续执行被拦截的方法
        return joinPoint.proceed()
    }
}