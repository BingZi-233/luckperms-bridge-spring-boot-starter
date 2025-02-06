package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 枚举类 QueryMode
 * 
 * 表示查询模式的类型，主要用于区分不同的查询方式。
 * 在整个代码结构中，QueryMode 可能用于决定如何执行查询操作，
 * 例如是否依赖于上下文信息来进行数据检索。
 */
enum class QueryMode {
    /** 
     * 上下文相关模式
     * 
     * 当使用此模式时，查询操作将依赖于特定的上下文信息，
     * 例如用户的角色、权限或其他环境因素，以便返回相关的数据。
     */
    CONTEXTUAL,

    /** 
     * 上下文无关模式
     * 
     * 此模式下，查询操作不依赖于任何上下文信息，
     * 通常用于全局查询或静态数据检索，返回的结果与用户的上下文无关。
     */
    NON_CONTEXTUAL
}