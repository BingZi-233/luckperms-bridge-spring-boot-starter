package online.bingzi.luck.perms.bridge.spring.boot.starter.exception

/**
 * 权限不足异常
 * 当用户没有足够权限访问资源时抛出此异常
 */
class PermissionDeniedException(
    message: String = "权限不足"
) : RuntimeException(message) 