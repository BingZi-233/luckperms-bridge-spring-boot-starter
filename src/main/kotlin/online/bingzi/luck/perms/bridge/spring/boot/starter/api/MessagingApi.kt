package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.CustomMessage
import retrofit2.Call
import retrofit2.http.*

/**
 * LuckPerms 消息服务API接口
 *
 * 提供消息服务相关的API操作，包括：
 * - 推送更新消息
 * - 推送用户更新
 * - 发送自定义消息
 */
interface MessagingApi {
    /**
     * 推送更新消息到消息服务
     */
    @POST("messaging/update")
    fun pushUpdate(): Call<Unit>

    /**
     * 推送用户更新消息到消息服务
     *
     * @param uniqueId 用户UUID
     */
    @POST("messaging/update/{uniqueId}")
    fun pushUserUpdate(@Path("uniqueId") uniqueId: String): Call<Unit>

    /**
     * 发送自定义消息到消息服务
     *
     * @param message 自定义消息内容
     */
    @POST("messaging/custom")
    fun sendCustomMessage(@Body message: CustomMessage): Call<Unit>
} 