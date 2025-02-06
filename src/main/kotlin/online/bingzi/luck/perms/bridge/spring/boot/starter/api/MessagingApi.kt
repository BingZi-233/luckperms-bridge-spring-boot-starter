package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.CustomMessage
import retrofit2.Call
import retrofit2.http.*

/**
 * LuckPerms 消息服务API接口
 *
 * 此接口定义了一系列与消息服务相关的操作，主要功能包括：
 * - 推送更新消息
 * - 推送用户更新
 * - 发送自定义消息
 * 
 * 该接口通过 Retrofit 库实现，与后端服务进行通信。
 */
interface MessagingApi {
    /**
     * 推送更新消息到消息服务
     * 
     * 此方法不需要任何参数，调用后会向消息服务发送一个更新请求。
     * 
     * @return 返回一个 Call<Unit> 对象，表示异步请求的结果。
     */
    @POST("messaging/update")
    fun pushUpdate(): Call<Unit>

    /**
     * 推送用户更新消息到消息服务
     *
     * 此方法用于推送特定用户的更新消息，需要提供用户的唯一标识符。
     * 
     * @param uniqueId 用户的 UUID，类型为 String，表示要更新的用户的唯一标识。
     *                  它应该是一个有效的 UUID 格式字符串。
     * @return 返回一个 Call<Unit> 对象，表示异步请求的结果。
     */
    @POST("messaging/update/{uniqueId}")
    fun pushUserUpdate(@Path("uniqueId") uniqueId: String): Call<Unit>

    /**
     * 发送自定义消息到消息服务
     *
     * 此方法允许用户发送自定义消息内容，适用于特殊的消息需求。
     * 
     * @param message 自定义消息内容，类型为 CustomMessage，包含消息的详细信息。
     *                该参数必须是有效的 CustomMessage 对象。
     * @return 返回一个 Call<Unit> 对象，表示异步请求的结果。
     */
    @POST("messaging/custom")
    fun sendCustomMessage(@Body message: CustomMessage): Call<Unit>
}