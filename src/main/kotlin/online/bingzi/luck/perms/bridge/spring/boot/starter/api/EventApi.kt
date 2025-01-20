package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Streaming

/**
 * 事件订阅接口
 */
interface EventApi {
    /**
     * 订阅日志广播事件
     * 使用SSE(Server-Sent Events)接收事件
     * 
     * @return 事件流
     */
    @GET("/event/log-broadcast")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribeLogBroadcastEvents(): Call<ResponseBody>

    /**
     * 订阅网络同步后事件
     * 使用SSE(Server-Sent Events)接收事件
     * 
     * @return 事件流
     */
    @GET("/event/post-network-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePostNetworkSyncEvents(): Call<ResponseBody>

    /**
     * 订阅同步后事件
     * 使用SSE(Server-Sent Events)接收事件
     * 
     * @return 事件流
     */
    @GET("/event/post-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePostSyncEvents(): Call<ResponseBody>

    /**
     * 订阅网络同步前事件
     * 使用SSE(Server-Sent Events)接收事件
     * 
     * @return 事件流
     */
    @GET("/event/pre-network-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePreNetworkSyncEvents(): Call<ResponseBody>

    /**
     * 订阅同步前事件
     * 使用SSE(Server-Sent Events)接收事件
     * 
     * @return 事件流
     */
    @GET("/event/pre-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePreSyncEvents(): Call<ResponseBody>

    /**
     * 订阅自定义消息事件
     * 使用SSE(Server-Sent Events)接收事件
     * 
     * @return 事件流
     */
    @GET("/event/custom-message-receive")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribeCustomMessageReceiveEvents(): Call<ResponseBody>
} 