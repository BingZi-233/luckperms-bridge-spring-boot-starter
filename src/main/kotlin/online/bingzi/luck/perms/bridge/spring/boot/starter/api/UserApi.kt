package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.*
import retrofit2.Call
import retrofit2.http.*

/**
 * LuckPerms 用户API接口
 *
 * 提供用户管理相关的API操作，包括：
 * - 用户基本信息的CRUD
 * - 用户权限节点管理
 * - 用户元数据管理
 * - 用户权限检查
 * - 用户晋升/降级操作
 */
interface UserApi {
    /**
     * 获取所有已存在的用户
     *
     * @return 用户UUID列表
     */
    @GET("user")
    fun getUsers(): Call<List<String>>

    /**
     * 创建新用户
     *
     * @param user 新用户信息
     * @return 用户创建结果
     */
    @POST("user")
    fun createUser(@Body user: User): Call<PlayerSaveResult>

    /**
     * 根据用户名或UUID查找用户
     *
     * @param username 用户名(可选)
     * @param uniqueId UUID(可选)
     * @return 用户查找结果
     */
    @GET("user/lookup")
    fun lookupUser(
        @Query("username") username: String? = null,
        @Query("uniqueId") uniqueId: String? = null
    ): Call<UserLookupResult>

    /**
     * 搜索具有指定节点的用户
     *
     * @param key 精确匹配的节点键
     * @param keyStartsWith 节点键前缀
     * @param metaKey 元数据键
     * @param type 节点类型
     * @return 用户搜索结果列表
     */
    @GET("user/search")
    fun searchUsers(
        @Query("key") key: String? = null,
        @Query("keyStartsWith") keyStartsWith: String? = null,
        @Query("metaKey") metaKey: String? = null,
        @Query("type") type: NodeType? = null
    ): Call<List<UserSearchResult>>

    /**
     * 获取指定用户的详细信息
     *
     * @param uniqueId 用户UUID
     * @return 用户详细信息
     */
    @GET("user/{uniqueId}")
    fun getUser(@Path("uniqueId") uniqueId: String): Call<User>

    /**
     * 更新用户信息
     *
     * @param uniqueId 用户UUID
     * @param username 新的用户名
     */
    @PATCH("user/{uniqueId}")
    fun updateUser(
        @Path("uniqueId") uniqueId: String,
        @Body username: String
    ): Call<Unit>

    /**
     * 删除用户
     *
     * @param uniqueId 用户UUID
     * @param playerDataOnly 是否仅删除玩家数据
     */
    @DELETE("user/{uniqueId}")
    fun deleteUser(
        @Path("uniqueId") uniqueId: String,
        @Query("playerDataOnly") playerDataOnly: Boolean? = null
    ): Call<Unit>

    /**
     * 获取用户的权限节点
     *
     * @param uniqueId 用户UUID
     * @return 权限节点列表
     */
    @GET("user/{uniqueId}/nodes")
    fun getUserNodes(@Path("uniqueId") uniqueId: String): Call<List<Node>>

    /**
     * 添加权限节点到用户
     *
     * @param uniqueId 用户UUID
     * @param node 权限节点
     * @param temporaryNodeMergeStrategy 临时节点合并策略
     * @return 更新后的节点列表
     */
    @POST("user/{uniqueId}/nodes")
    fun addUserNode(
        @Path("uniqueId") uniqueId: String,
        @Body node: Node,
        @Query("temporaryNodeMergeStrategy") temporaryNodeMergeStrategy: String? = null
    ): Call<List<Node>>

    /**
     * 批量添加权限节点到用户
     *
     * @param uniqueId 用户UUID
     * @param nodes 权限节点列表
     * @param temporaryNodeMergeStrategy 临时节点合并策略
     * @return 更新后的节点列表
     */
    @PATCH("user/{uniqueId}/nodes")
    fun addUserNodes(
        @Path("uniqueId") uniqueId: String,
        @Body nodes: List<Node>,
        @Query("temporaryNodeMergeStrategy") temporaryNodeMergeStrategy: String? = null
    ): Call<List<Node>>

    /**
     * 设置用户的权限节点(替换现有节点)
     *
     * @param uniqueId 用户UUID
     * @param nodes 新的权限节点列表
     */
    @PUT("user/{uniqueId}/nodes")
    fun setUserNodes(
        @Path("uniqueId") uniqueId: String,
        @Body nodes: List<Node>
    ): Call<Unit>

    /**
     * 删除用户的权限节点
     *
     * @param uniqueId 用户UUID
     * @param nodes 要删除的节点列表，为空则删除所有节点
     */
    @HTTP(method = "DELETE", path = "user/{uniqueId}/nodes", hasBody = true)
    fun deleteUserNodes(
        @Path("uniqueId") uniqueId: String,
        @Body nodes: List<Node>? = null
    ): Call<Unit>

    /**
     * 获取用户的元数据
     *
     * @param uniqueId 用户UUID
     * @return 用户元数据
     */
    @GET("user/{uniqueId}/meta")
    fun getUserMeta(@Path("uniqueId") uniqueId: String): Call<Metadata>

    /**
     * 检查用户是否拥有指定权限
     *
     * @param uniqueId 用户UUID
     * @param permission 权限节点
     * @return 权限检查结果
     */
    @GET("user/{uniqueId}/permission-check")
    fun checkUserPermission(
        @Path("uniqueId") uniqueId: String,
        @Query("permission") permission: String
    ): Call<PermissionCheckResult>

    /**
     * 使用自定义查询选项检查用户权限
     *
     * @param uniqueId 用户UUID
     * @param request 权限检查请求
     * @return 权限检查结果
     */
    @POST("user/{uniqueId}/permission-check")
    fun checkUserPermissionWithOptions(
        @Path("uniqueId") uniqueId: String,
        @Body request: PermissionCheckRequest
    ): Call<PermissionCheckResult>

    /**
     * 在指定轨道上提升用户
     *
     * @param uniqueId 用户UUID
     * @param request 轨道请求
     * @return 提升结果
     */
    @POST("user/{uniqueId}/promote")
    fun promoteUser(
        @Path("uniqueId") uniqueId: String,
        @Body request: TrackRequest
    ): Call<PromotionResult>

    /**
     * 在指定轨道上降级用户
     *
     * @param uniqueId 用户UUID
     * @param request 轨道请求
     * @return 降级结果
     */
    @POST("user/{uniqueId}/demote")
    fun demoteUser(
        @Path("uniqueId") uniqueId: String,
        @Body request: TrackRequest
    ): Call<DemotionResult>
} 