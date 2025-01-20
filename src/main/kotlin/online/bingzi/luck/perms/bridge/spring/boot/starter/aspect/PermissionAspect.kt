package online.bingzi.luck.perms.bridge.spring.boot.starter.aspect

import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequirePermission
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode
import online.bingzi.luck.perms.bridge.spring.boot.starter.exception.PermissionDeniedException
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.PermissionService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

/**
 * 权限检查切面
 * 用于处理RequirePermission注解的权限检查逻辑
 */
@Aspect
@Component
class PermissionAspect(
    private val permissionService: PermissionService,
    private val userIdentityService: UserIdentityService
) {
    /**
     * 权限检查切面处理
     * 拦截带有@RequirePermission注解的方法,进行权限检查
     */
    @Around("@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequirePermission)")
    fun checkPermission(joinPoint: ProceedingJoinPoint): Any {
        // 获取方法签名
        val signature = joinPoint.signature as MethodSignature
        // 获取方法上的注解
        val annotation = signature.method.getAnnotation(RequirePermission::class.java)
        
        // 获取当前用户ID
        val userId = userIdentityService.getCurrentUserId()
        
        // 获取需要检查的权限列表并转换为Array<String>
        val permissions = annotation.value.toList().toTypedArray()
        
        // 检查权限
        val hasPermission = when (annotation.mode) {
            CheckMode.ANY -> permissionService.hasAnyPermission(userId, permissions)
            CheckMode.ALL -> permissionService.hasAllPermissions(userId, permissions)
        }
        
        // 如果没有权限,抛出异常
        if (!hasPermission) {
            throw PermissionDeniedException(annotation.message)
        }
        
        // 有权限则继续执行
        return joinPoint.proceed()
    }
}