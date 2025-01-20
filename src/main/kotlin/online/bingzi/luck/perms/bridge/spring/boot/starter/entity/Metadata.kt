package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

/**
 * 元数据实体类
 *
 * 用于存储实体（用户或组）的额外信息。
 * 包括自定义元数据、前缀、后缀和主组设置。
 *
 * @property meta 自定义元数据映射，可存储任意键值对数据
 * @property prefix 前缀，显示在名字前面的文本，支持颜色代码
 * @property suffix 后缀，显示在名字后面的文本，支持颜色代码
 * @property primaryGroup 主组，指定实体的主要所属组
 */
data class Metadata(
    val meta: Map<String, String> = emptyMap(),
    val prefix: String? = null,
    val suffix: String? = null,
    val primaryGroup: String? = null
) 