package online.bingzi.luck.perms.bridge.spring.boot.starter.aspect

import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequireGroup
import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.CheckMode
import online.bingzi.luck.perms.bridge.spring.boot.starter.exception.PermissionDeniedException
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.GroupService
import online.bingzi.luck.perms.bridge.spring.boot.starter.service.UserIdentityService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

/**
 * 组检查切面
 * 用于处理RequireGroup注解。
 */
@Aspect
@Component
class GroupAspect(
    private val groupService: GroupService,
    private val userIdentityService: UserIdentityService
) {
    /**
     * 组检查切面处理
     * 拦截带有@RequireGroup注解的方法,进行组权限检查
     */
    @Around("@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequireGroup)")
    fun checkGroup(joinPoint: ProceedingJoinPoint): Any {
        // 获取方法签名
        val signature = joinPoint.signature as MethodSignature
        // 获取方法上的注解
        val annotation = signature.method.getAnnotation(RequireGroup::class.java)
        
        // 获取当前用户ID
        val userId = userIdentityService.getCurrentUserId()
        
        // 获取需要检查的组列表
        val groups = annotation.value
        
        // 检查组权限
        val hasPermission = when (annotation.mode) {
            CheckMode.ANY -> groupService.isInAnyGroup(userId, groups)
            CheckMode.ALL -> groupService.isInAllGroups(userId, groups)
        }
        
        // 如果没有权限,抛出异常
        if (!hasPermission) {
            throw PermissionDeniedException(annotation.message)
        }
        
        // 有权限则继续执行
        return joinPoint.proceed()
    }
} 