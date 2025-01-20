package online.bingzi.luck.perms.bridge.spring.boot.starter.event

/**
 * LuckPerms事件基础接口
 * 所有LuckPerms事件都必须实现此接口
 * 用于统一事件处理和类型识别
 */
interface LuckPermsEvent {
    /**
     * 获取事件类型
     * @return 事件类型的字符串标识符
     */
    fun getType(): String
} 