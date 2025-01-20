package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Action
import retrofit2.Call
import retrofit2.http.*

/**
 * LuckPerms 操作记录API接口
 *
 * 提供操作记录相关的API操作，包括：
 * - 查询操作记录
 * - 提交新的操作记录
 */
interface ActionApi {
    /**
     * 查询操作记录
     *
     * @param pageSize 每页大小
     * @param pageNumber 页码
     * @param source 按来源用户UUID过滤
     * @param user 按目标用户UUID过滤
     * @param group 按目标组名过滤
     * @param track 按目标轨道名过滤
     * @param search 按来源名称、目标名称或描述搜索
     * @return 操作记录列表和总数
     */
    @GET("action")
    fun getActions(
        @Query("pageSize") pageSize: Int? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("source") source: String? = null,
        @Query("user") user: String? = null,
        @Query("group") group: String? = null,
        @Query("track") track: String? = null,
        @Query("search") search: String? = null
    ): Call<Map<String, Any>>

    /**
     * 提交新的操作记录
     *
     * @param action 操作记录信息
     */
    @POST("action")
    fun submitAction(@Body action: Action): Call<Unit>
} 