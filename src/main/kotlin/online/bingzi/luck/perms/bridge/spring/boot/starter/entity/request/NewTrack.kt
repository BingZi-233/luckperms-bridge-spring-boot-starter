package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

/**
 * 创建新轨道的请求实体
 *
 * 该数据类用于表示创建新轨道时所需的请求数据。
 * 包含轨道的基本信息，主要是轨道的名称。
 *
 * @property name 轨道名称，类型为 String，表示新轨道的唯一标识
 */
data class NewTrack(
    val name: String // 轨道名称，表示新轨道的名称，不能为空
) 