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
 * 此类用于处理带有@RequirePermission注解的方法的权限检查逻辑。
 * 它通过AOP（面向切面编程）实现，对标记的方法进行拦截并执行权限验证。
 */
@Aspect
@Component
class PermissionAspect(
    private val permissionService: PermissionService, // 权限服务，用于检查用户权限
    private val userIdentityService: UserIdentityService // 用户身份服务，用于获取当前用户ID
) {
    /**
     * 权限检查切面处理
     * 此方法拦截带有@RequirePermission注解的方法，进行权限检查。
     * @param joinPoint 连接点，提供对目标方法的访问。
     * @return 返回目标方法的执行结果，如果权限检查通过，则执行目标方法；否则抛出异常。
     * @throws PermissionDeniedException 如果用户没有权限，则抛出此异常。
     */
    @Around("@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequirePermission)")
    fun checkPermission(joinPoint: ProceedingJoinPoint): Any {
        // 获取方法签名，以便从中获取方法相关的信息
        val signature = joinPoint.signature as MethodSignature
        // 获取目标方法上的@RequirePermission注解
        val annotation = signature.method.getAnnotation(RequirePermission::class.java)
        
        // 获取当前用户ID
        val userId = userIdentityService.getCurrentUserId()
        
        // 获取需要检查的权限列表并转换为Array<String>
        // annotation.value是需要的权限
        val permissions = annotation.value.toList().toTypedArray()
        
        // 根据注解的模式检查权限
        val hasPermission = when (annotation.mode) {
            CheckMode.ANY -> permissionService.hasAnyPermission(userId, permissions) // 如果模式是ANY，检查是否有任一权限
            CheckMode.ALL -> permissionService.hasAllPermissions(userId, permissions) // 如果模式是ALL，检查是否有所有权限
        }
        
        // 如果没有权限，抛出PermissionDeniedException异常
        if (!hasPermission) {
            throw PermissionDeniedException(annotation.message) // 使用注解中的消息作为异常信息
        }
        
        // 有权限则继续执行目标方法，并返回执行结果
        return joinPoint.proceed()
    }
}