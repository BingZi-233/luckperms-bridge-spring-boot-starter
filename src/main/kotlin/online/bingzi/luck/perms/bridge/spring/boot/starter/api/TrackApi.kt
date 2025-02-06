package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Track
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.NewTrack
import retrofit2.Call
import retrofit2.http.*

/**
 * LuckPerms 轨道API接口
 *
 * 该接口提供对轨道管理的相关API操作，包括：
 * - 获取所有轨道的列表
 * - 创建新的轨道
 * - 获取特定轨道的信息
 * - 更新已有轨道的信息
 * - 删除指定轨道
 */
interface TrackApi {
    /**
     * 获取所有轨道
     *
     * 此方法用于获取系统中所有轨道的名称列表。
     *
     * @return 轨道名称列表，类型为 List<String>
     */
    @GET("track")
    fun getTracks(): Call<List<String>>

    /**
     * 创建新轨道
     *
     * 此方法用于创建一个新的轨道。
     *
     * @param track 新轨道信息，类型为 NewTrack，包含新轨道的详细信息
     * @return 创建的轨道信息，类型为 Track，包含新创建轨道的详细信息
     */
    @POST("track")
    fun createTrack(@Body track: NewTrack): Call<Track>

    /**
     * 获取指定轨道信息
     *
     * 此方法用于获取特定轨道的详细信息。
     *
     * @param trackName 轨道名称，类型为 String，表示要获取信息的轨道名称
     * @return 轨道信息，类型为 Track，包含指定轨道的详细信息
     */
    @GET("track/{trackName}")
    fun getTrack(@Path("trackName") trackName: String): Call<Track>

    /**
     * 更新轨道
     *
     * 此方法用于更新指定轨道的信息，例如更新其关联的组列表。
     *
     * @param trackName 轨道名称，类型为 String，表示要更新的轨道名称
     * @param groups 新的组列表，类型为 List<String>，表示要更新的组名列表
     */
    @PATCH("track/{trackName}")
    fun updateTrack(
        @Path("trackName") trackName: String,
        @Body groups: List<String>
    ): Call<Unit>

    /**
     * 删除轨道
     *
     * 此方法用于删除指定的轨道。
     *
     * @param trackName 轨道名称，类型为 String，表示要删除的轨道名称
     */
    @DELETE("track/{trackName}")
    fun deleteTrack(@Path("trackName") trackName: String): Call<Unit>
}