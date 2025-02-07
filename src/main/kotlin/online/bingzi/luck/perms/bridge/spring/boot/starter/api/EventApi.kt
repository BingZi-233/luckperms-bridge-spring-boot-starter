package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Streaming

/**
 * 事件订阅接口
 * 
 * 此接口定义了一系列方法，用于通过Server-Sent Events (SSE) 订阅不同类型的事件。
 * 调用这些方法可以接收实时事件流，包括日志广播、网络同步事件、以及自定义消息事件等。
 */
interface EventApi {
    /**
     * 订阅日志广播事件
     * 
     * 通过此方法，客户端可以订阅日志广播事件，接收实时的日志消息。
     * 使用SSE(Server-Sent Events)接收事件。
     * 
     * @return 返回一个Call对象，表示事件流，类型为ResponseBody。
     * 该流包含服务器推送的事件数据。
     */
    @GET("/event/log-broadcast")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribeLogBroadcastEvents(): Call<ResponseBody>

    /**
     * 订阅网络同步后事件
     * 
     * 使用此方法，客户端可以订阅网络同步完成后的事件。
     * 使用SSE(Server-Sent Events)接收事件。
     * 
     * @return 返回一个Call对象，表示事件流，类型为ResponseBody。
     * 该流包含网络同步后服务器推送的事件数据。
     */
    @GET("/event/post-network-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePostNetworkSyncEvents(): Call<ResponseBody>

    /**
     * 订阅同步后事件
     * 
     * 通过此方法，客户端可以订阅数据同步完成后的事件。
     * 使用SSE(Server-Sent Events)接收事件。
     * 
     * @return 返回一个Call对象，表示事件流，类型为ResponseBody。
     * 该流包含同步后服务器推送的事件数据。
     */
    @GET("/event/post-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePostSyncEvents(): Call<ResponseBody>

    /**
     * 订阅网络同步前事件
     * 
     * 使用此方法，客户端可以订阅网络同步开始前的事件。
     * 使用SSE(Server-Sent Events)接收事件。
     * 
     * @return 返回一个Call对象，表示事件流，类型为ResponseBody。
     * 该流包含网络同步前服务器推送的事件数据。
     */
    @GET("/event/pre-network-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePreNetworkSyncEvents(): Call<ResponseBody>

    /**
     * 订阅同步前事件
     * 
     * 通过此方法，客户端可以订阅数据同步开始前的事件。
     * 使用SSE(Server-Sent Events)接收事件。
     * 
     * @return 返回一个Call对象，表示事件流，类型为ResponseBody。
     * 该流包含同步前服务器推送的事件数据。
     */
    @GET("/event/pre-sync")
    @Headers("Accept: text/event-stream")
    @Streaming
    fun subscribePreSyncEvents(): Call<ResponseBody>
} 