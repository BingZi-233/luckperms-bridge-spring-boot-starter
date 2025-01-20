package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Group
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Metadata
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Node
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.NewGroup
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.PermissionCheckRequest
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result.GroupSearchResult
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result.PermissionCheckResult
import retrofit2.Call
import retrofit2.http.*

/**
 * LuckPerms 组API接口
 *
 * 提供组管理相关的API操作，包括：
 * - 组基本信息的CRUD
 * - 组权限节点管理
 * - 组元数据管理
 * - 组权限检查
 */
interface GroupApi {
    /**
     * 获取所有组
     *
     * @return 组名称列表
     */
    @GET("group")
    fun getGroups(): Call<List<String>>

    /**
     * 创建新组
     *
     * @param group 新组信息
     * @return 创建的组信息
     */
    @POST("group")
    fun createGroup(@Body group: NewGroup): Call<Group>

    /**
     * 搜索具有指定节点的组
     *
     * @param key 精确匹配的节点键
     * @param keyStartsWith 节点键前缀
     * @param metaKey 元数据键
     * @param type 节点类型
     * @return 组搜索结果列表
     */
    @GET("group/search")
    fun searchGroups(
        @Query("key") key: String? = null,
        @Query("keyStartsWith") keyStartsWith: String? = null,
        @Query("metaKey") metaKey: String? = null,
        @Query("type") type: String? = null
    ): Call<List<GroupSearchResult>>

    /**
     * 获取指定组信息
     *
     * @param groupName 组名称
     * @return 组信息
     */
    @GET("group/{groupName}")
    fun getGroup(@Path("groupName") groupName: String): Call<Group>

    /**
     * 删除组
     *
     * @param groupName 组名称
     */
    @DELETE("group/{groupName}")
    fun deleteGroup(@Path("groupName") groupName: String): Call<Unit>

    /**
     * 获取组的权限节点
     *
     * @param groupName 组名称
     * @return 权限节点列表
     */
    @GET("group/{groupName}/nodes")
    fun getGroupNodes(@Path("groupName") groupName: String): Call<List<Node>>

    /**
     * 添加权限节点到组
     *
     * @param groupName 组名称
     * @param node 权限节点
     * @param temporaryNodeMergeStrategy 临时节点合并策略
     * @return 更新后的节点列表
     */
    @POST("group/{groupName}/nodes")
    fun addGroupNode(
        @Path("groupName") groupName: String,
        @Body node: Node,
        @Query("temporaryNodeMergeStrategy") temporaryNodeMergeStrategy: String? = null
    ): Call<List<Node>>

    /**
     * 批量添加权限节点到组
     *
     * @param groupName 组名称
     * @param nodes 权限节点列表
     * @param temporaryNodeMergeStrategy 临时节点合并策略
     * @return 更新后的节点列表
     */
    @PATCH("group/{groupName}/nodes")
    fun addGroupNodes(
        @Path("groupName") groupName: String,
        @Body nodes: List<Node>,
        @Query("temporaryNodeMergeStrategy") temporaryNodeMergeStrategy: String? = null
    ): Call<List<Node>>

    /**
     * 设置组的权限节点(替换现有节点)
     *
     * @param groupName 组名称
     * @param nodes 新的权限节点列表
     */
    @PUT("group/{groupName}/nodes")
    fun setGroupNodes(
        @Path("groupName") groupName: String,
        @Body nodes: List<Node>
    ): Call<Unit>

    /**
     * 删除组的权限节点
     *
     * @param groupName 组名称
     * @param nodes 要删除的节点列表，为空则删除所有节点
     */
    @HTTP(method = "DELETE", path = "group/{groupName}/nodes", hasBody = true)
    fun deleteGroupNodes(
        @Path("groupName") groupName: String,
        @Body nodes: List<Node>? = null
    ): Call<Unit>

    /**
     * 获取组的元数据
     *
     * @param groupName 组名称
     * @return 组元数据
     */
    @GET("group/{groupName}/meta")
    fun getGroupMeta(@Path("groupName") groupName: String): Call<Metadata>

    /**
     * 检查组是否拥有指定权限
     *
     * @param groupName 组名称
     * @param permission 权限节点
     * @return 权限检查结果
     */
    @GET("group/{groupName}/permission-check")
    fun checkGroupPermission(
        @Path("groupName") groupName: String,
        @Query("permission") permission: String
    ): Call<PermissionCheckResult>

    /**
     * 使用自定义查询选项检查组权限
     *
     * @param groupName 组名称
     * @param request 权限检查请求
     * @return 权限检查结果
     */
    @POST("group/{groupName}/permission-check")
    fun checkGroupPermissionWithOptions(
        @Path("groupName") groupName: String,
        @Body request: PermissionCheckRequest
    ): Call<PermissionCheckResult>
} 