package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.enums

/**
 * 用于定义权限查询模式的枚举类。
 * 该枚举类提供了两种不同的权限查询方式，分别为上下文相关和非上下文相关。
 * 在权限管理系统中，根据不同的业务需求选择合适的查询模式以提升系统的灵活性和安全性。
 */
enum class QueryMode {
    /**
     * 上下文相关查询模式。
     * 该模式依据当前的上下文环境进行权限查询，适用于需要考虑用户状态或环境因素的场景。
     */
    CONTEXTUAL,

    /**
     * 非上下文相关查询模式。
     * 该模式不考虑当前上下文环境，而是直接进行权限查询，适用于静态权限检查或全局权限验证。
     */
    NON_CONTEXTUAL
}