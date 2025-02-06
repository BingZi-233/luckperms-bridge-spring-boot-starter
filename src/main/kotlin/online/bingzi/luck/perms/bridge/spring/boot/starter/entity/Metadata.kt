package online.bingzi.luck.perms.bridge.spring.boot.starter.entity

/**
 * 元数据实体类
 *
 * 该类用于存储与实体（如用户或组）相关的额外信息。
 * 主要功能是保存自定义的元数据、前缀、后缀，以及指定实体的主组设置。
 * 通过这个类，用户可以灵活地为实体添加额外的信息，从而扩展实体的功能和表现。
 *
 * @property meta 自定义元数据映射，类型为 Map<String, String>，用于存储任意的键值对数据。
 *                 例如，可以用于存储用户的个性化设置或组的特定属性。
 * @property prefix 前缀，类型为 String?，可选。用于显示在实体名称前的文本，支持颜色代码。
 *                   例如，可以为用户名称添加前缀以指示其角色。
 * @property suffix 后缀，类型为 String?，可选。用于显示在实体名称后面的文本，支持颜色代码。
 *                   例如，可以为用户名称添加后缀以提供额外信息。
 * @property primaryGroup 主组，类型为 String?，可选。用于指定实体的主要所属组。
 *                       例如，可以用来指明用户所属的核心团队或组织。
 */
data class Metadata(
    val meta: Map<String, String> = emptyMap(), // 初始化为空的自定义元数据映射
    val prefix: String? = null, // 可选的前缀，默认为 null
    val suffix: String? = null, // 可选的后缀，默认为 null
    val primaryGroup: String? = null // 可选的主组，默认为 null
) 