package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Track
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.NewTrack
import retrofit2.Call
import retrofit2.http.*

/**
 * LuckPerms 轨道API接口
 *
 * 提供轨道管理相关的API操作，包括：
 * - 轨道基本信息的CRUD
 * - 轨道组管理
 */
interface TrackApi {
    /**
     * 获取所有轨道
     *
     * @return 轨道名称列表
     */
    @GET("track")
    fun getTracks(): Call<List<String>>

    /**
     * 创建新轨道
     *
     * @param track 新轨道信息
     * @return 创建的轨道信息
     */
    @POST("track")
    fun createTrack(@Body track: NewTrack): Call<Track>

    /**
     * 获取指定轨道信息
     *
     * @param trackName 轨道名称
     * @return 轨道信息
     */
    @GET("track/{trackName}")
    fun getTrack(@Path("trackName") trackName: String): Call<Track>

    /**
     * 更新轨道
     *
     * @param trackName 轨道名称
     * @param groups 新的组列表
     */
    @PATCH("track/{trackName}")
    fun updateTrack(
        @Path("trackName") trackName: String,
        @Body groups: List<String>
    ): Call<Unit>

    /**
     * 删除轨道
     *
     * @param trackName 轨道名称
     */
    @DELETE("track/{trackName}")
    fun deleteTrack(@Path("trackName") trackName: String): Call<Unit>
} 