package online.bingzi.luck.perms.bridge.spring.boot.starter.aspect

import online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequireGroup
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.CheckMode
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
 * 该类用于处理带有@RequireGroup注解的方法，并在方法调用前进行用户组权限的检查。
 * 主要依赖于GroupService和UserIdentityService来实现权限验证。
 */
@Aspect
@Component
class GroupAspect(
    // 组服务，用于检查用户的组权限
    private val groupService: GroupService,
    // 用户身份服务，用于获取当前用户的ID
    private val userIdentityService: UserIdentityService
) {
    /**
     * 组检查切面处理
     * 拦截带有@RequireGroup注解的方法，进行组权限检查。
     * 
     * @param joinPoint 连接点，包含被拦截的方法信息
     * @return Any 被拦截的方法执行结果
     * @throws PermissionDeniedException 如果用户没有足够的权限
     */
    @Around("@annotation(online.bingzi.luck.perms.bridge.spring.boot.starter.annotation.RequireGroup)")
    fun checkGroup(joinPoint: ProceedingJoinPoint): Any {
        // 获取方法签名，便于获取方法的相关信息
        val signature = joinPoint.signature as MethodSignature
        // 获取方法上的RequireGroup注解
        val annotation = signature.method.getAnnotation(RequireGroup::class.java)
        
        // 获取当前用户ID
        val userId = userIdentityService.getCurrentUserId()
        
        // 获取需要检查的组列表并将其转换为Array<String>
        val groups = annotation.value.toList().toTypedArray()
        
        // 检查组权限，根据注解中指定的模式来判断
        val hasPermission = when (annotation.mode) {
            CheckMode.ANY -> groupService.isInAnyGroup(userId, groups) // 如果模式为ANY，检查用户是否在任意一个组中
            CheckMode.ALL -> groupService.isInAllGroups(userId, groups) // 如果模式为ALL，检查用户是否在所有组中
        }
        
        // 如果没有权限,抛出PermissionDeniedException异常
        if (!hasPermission) {
            throw PermissionDeniedException(annotation.message) // 使用注解中定义的消息构造异常
        }
        
        // 如果有权限，则继续执行被拦截的方法
        return joinPoint.proceed()
    }
}