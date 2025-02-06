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
 * 该接口提供了与LuckPerms相关的组管理操作，包括：
 * - 获取所有组的信息
 * - 创建、删除、更新组
 * - 管理组的权限节点
 * - 查询组的元数据和权限
 */
interface GroupApi {
    /**
     * 获取所有组
     *
     * 该方法用于获取系统中所有组的名称列表。
     *
     * @return 组名称列表
     */
    @GET("group")
    fun getGroups(): Call<List<String>>

    /**
     * 创建新组
     *
     * 该方法用于创建一个新的组，组的详细信息通过参数传入。
     *
     * @param group 新组信息，包含组的名称和其他属性
     * @return 创建的组信息
     */
    @POST("group")
    fun createGroup(@Body group: NewGroup): Call<Group>

    /**
     * 搜索具有指定节点的组
     *
     * 该方法允许用户根据节点的键或元数据键搜索符合条件的组。
     *
     * @param key 精确匹配的节点键，类型为String，默认为null
     * @param keyStartsWith 节点键前缀，类型为String，默认为null
     * @param metaKey 元数据键，类型为String，默认为null
     * @param type 节点类型，类型为String，默认为null
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
     * 该方法根据组名称获取对应组的详细信息。
     *
     * @param groupName 组名称，类型为String
     * @return 组信息
     */
    @GET("group/{groupName}")
    fun getGroup(@Path("groupName") groupName: String): Call<Group>

    /**
     * 删除组
     *
     * 该方法用于删除指定名称的组。
     *
     * @param groupName 组名称，类型为String
     */
    @DELETE("group/{groupName}")
    fun deleteGroup(@Path("groupName") groupName: String): Call<Unit>

    /**
     * 获取组的权限节点
     *
     * 该方法用于获取指定组的所有权限节点。
     *
     * @param groupName 组名称，类型为String
     * @return 权限节点列表
     */
    @GET("group/{groupName}/nodes")
    fun getGroupNodes(@Path("groupName") groupName: String): Call<List<Node>>

    /**
     * 添加权限节点到组
     *
     * 该方法用于将一个权限节点添加到指定的组中。
     *
     * @param groupName 组名称，类型为String
     * @param node 权限节点，类型为Node
     * @param temporaryNodeMergeStrategy 临时节点合并策略，类型为String，默认为null
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
     * 该方法用于将多个权限节点批量添加到指定的组中。
     *
     * @param groupName 组名称，类型为String
     * @param nodes 权限节点列表，类型为List<Node>
     * @param temporaryNodeMergeStrategy 临时节点合并策略，类型为String，默认为null
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
     * 该方法用于替换指定组的所有权限节点，使用新的节点列表。
     *
     * @param groupName 组名称，类型为String
     * @param nodes 新的权限节点列表，类型为List<Node>
     */
    @PUT("group/{groupName}/nodes")
    fun setGroupNodes(
        @Path("groupName") groupName: String,
        @Body nodes: List<Node>
    ): Call<Unit>

    /**
     * 删除组的权限节点
     *
     * 该方法用于删除指定组的权限节点，可以选择性地删除指定的节点，若不传入节点列表则删除所有节点。
     *
     * @param groupName 组名称，类型为String
     * @param nodes 要删除的节点列表，为空则删除所有节点，类型为List<Node>?，默认为null
     */
    @HTTP(method = "DELETE", path = "group/{groupName}/nodes", hasBody = true)
    fun deleteGroupNodes(
        @Path("groupName") groupName: String,
        @Body nodes: List<Node>? = null
    ): Call<Unit>

    /**
     * 获取组的元数据
     *
     * 该方法用于获取指定组的元数据。
     *
     * @param groupName 组名称，类型为String
     * @return 组元数据
     */
    @GET("group/{groupName}/meta")
    fun getGroupMeta(@Path("groupName") groupName: String): Call<Metadata>

    /**
     * 检查组是否拥有指定权限
     *
     * 该方法用于检查指定组是否拥有某个权限节点。
     *
     * @param groupName 组名称，类型为String
     * @param permission 权限节点，类型为String
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
     * 该方法允许用户使用自定义的请求体来检查组的权限。
     *
     * @param groupName 组名称，类型为String
     * @param request 权限检查请求，类型为PermissionCheckRequest
     * @return 权限检查结果
     */
    @POST("group/{groupName}/permission-check")
    fun checkGroupPermissionWithOptions(
        @Path("groupName") groupName: String,
        @Body request: PermissionCheckRequest
    ): Call<PermissionCheckResult>
}