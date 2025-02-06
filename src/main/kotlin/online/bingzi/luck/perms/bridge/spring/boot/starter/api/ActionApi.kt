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
 * 
 * 该接口定义了与操作记录相关的HTTP请求方法，以便客户端能够访问和管理操作记录。
 */
interface ActionApi {
    /**
     * 查询操作记录
     *
     * 该方法用于获取操作记录的列表，可以通过多种参数进行过滤和分页。
     *
     * @param pageSize 每页大小，类型为 Int?，可选，表示每一页返回的记录数，通常设置为正整数。
     * @param pageNumber 页码，类型为 Int?，可选，表示请求的页码，通常设置为正整数，第一页为 1。
     * @param source 按来源用户UUID过滤，类型为 String?，可选，表示操作记录的来源用户的UUID。
     * @param user 按目标用户UUID过滤，类型为 String?，可选，表示操作记录的目标用户的UUID。
     * @param group 按目标组名过滤，类型为 String?，可选，表示操作记录的目标组名。
     * @param track 按目标轨道名过滤，类型为 String?，可选，表示操作记录的目标轨道名称。
     * @param search 按来源名称、目标名称或描述搜索，类型为 String?，可选，表示用于搜索的关键字。
     * 
     * @return 返回一个 Call 对象，包含操作记录的列表和总记录数，类型为 Map<String, Any>。
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
     * 该方法用于提交一个新的操作记录。
     *
     * @param action 操作记录信息，类型为 Action，表示要提交的操作记录的具体内容。
     * 
     * @return 返回一个 Call 对象，类型为 Unit，表示提交操作的结果，通常用于确认请求的成功与否。
     */
    @POST("action")
    fun submitAction(@Body action: Action): Call<Unit>
}