package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

/**
 * 权限检查模式
 */
enum class CheckMode {
    /**
     * 任一权限满足即可
     */
    ANY,
    
    /**
     * 必须满足所有权限
     */
    ALL
}