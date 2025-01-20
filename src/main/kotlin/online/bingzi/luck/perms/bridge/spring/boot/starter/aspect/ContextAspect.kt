package online.bingzi.luck.perms.bridge.spring.boot.starter.aspect

import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.WithContext
import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.CheckMode
import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequirePermission
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
 * 用于处理WithContext注解的上下文检查逻辑
 * 注意：此切面的优先级高于权限检查切面,以确保在权限检查之前先获取上下文
 */
@Aspect
@Component
@Order(0)
class ContextAspect(
    private val contextService: ContextService,
    private val userIdentityService: UserIdentityService
) {
    /**
     * 上下文切面处理
     * 拦截带有@WithContext和@RequirePermission注解的方法,进行上下文权限检查
     */
    @Around("@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.WithContext) && " +
            "@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequirePermission)")
    fun checkContext(joinPoint: ProceedingJoinPoint): Any {
        // 获取方法签名
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        
        // 获取上下文注解
        val contexts = method.getAnnotationsByType(WithContext::class.java)
        // 获取权限注解
        val permission = method.getAnnotation(RequirePermission::class.java)
        
        // 获取当前用户ID
        val userId = userIdentityService.getCurrentUserId()
        
        // 将上下文转换为Map
        val contextMap = contexts.associate { it.key to it.value }
        
        // 检查每个权限是否在上下文中有效
        val hasPermission = when (permission.mode) {
            CheckMode.ANY -> permission.value.any { perm ->
                contextService.hasPermissionInAnyContext(userId, perm, contextMap)
            }
            CheckMode.ALL -> permission.value.all { perm ->
                contextService.hasPermissionInAllContexts(userId, perm, contextMap)
            }
        }
        
        // 如果没有权限,抛出异常
        if (!hasPermission) {
            throw PermissionDeniedException(permission.message)
        }
        
        // 有权限则继续执行
        return joinPoint.proceed()
    }
} 