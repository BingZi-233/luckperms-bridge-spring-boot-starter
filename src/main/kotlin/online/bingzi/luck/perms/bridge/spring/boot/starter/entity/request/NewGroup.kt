package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 创建新组的请求实体
 *
 * 该数据类用于表示创建新组的请求数据，包含组的基本信息。它主要用于接收前端传来的组创建请求，便于控制器处理。
 *
 * @property name 组名称，表示要创建的组的名称，类型为字符串。该属性是必需的，不能为空。
 */
data class NewGroup(
    val name: String // 组名称，不能为空字符串
) 