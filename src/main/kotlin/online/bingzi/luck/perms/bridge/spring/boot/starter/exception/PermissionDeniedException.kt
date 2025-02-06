package online.bingzi.luck.perms.bridge.spring.boot.starter.exception

/**
 * 权限不足异常类
 * 
 * 该类用于表示用户在尝试访问某个资源时，没有足够的权限。这通常用于权限管理系统中，当用户的权限不足以执行某个操作时，会抛出此异常。
 *
 * 继承自 RuntimeException，表示这是一个运行时异常，通常不需要在方法签名中强制声明。
 *
 * @property message 异常消息，默认值为 "权限不足"。
 */
class PermissionDeniedException(
    message: String = "权限不足"  // 异常消息，提供关于错误的具体信息
) : RuntimeException(message) // 调用父类的构造函数，传入异常消息