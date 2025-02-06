package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

/**
 * 权限检查模式的枚举类
 * 
 * 该枚举类定义了两种权限检查模式，主要用于在权限验证过程中指定
 * 需要采用的策略。它在整个权限管理系统中发挥着重要的作用。
 */
enum class CheckMode {
    /**
     * 任一权限满足即可
     * 
     * 该模式表示在进行权限检查时，只需满足其中任意一个权限即可通过验证。
     * 适用于需要灵活控制的场景。
     */
    ANY,
    
    /**
     * 必须满足所有权限
     * 
     * 该模式表示在进行权限检查时，必须同时满足所有指定的权限才能通过验证。
     * 适用于对权限要求较高的场景。
     */
    ALL
}