package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.health.Health
import retrofit2.Call
import retrofit2.http.GET

/**
 * 健康检查接口
 * 此接口用于检查LuckPerms系统的运行状态，提供系统健康状况的信息。
 * 主要功能是通过调用对应的API获取系统的健康状态，包括系统的整体健康情况、存储系统的连接状态及其响应时间。
 */
interface HealthApi {
    /**
     * 获取系统健康状态
     * 
     * 此接口会访问"/health"路径，并返回LuckPerms系统的健康状况。 
     * 返回的数据包括：
     * 1. 系统整体健康状态（如是否正常运行）
     * 2. 存储系统的连接状态（如是否能够连接到数据库）
     * 3. 存储系统的响应时间（如查询的耗时）
     * 4. 如果系统存在问题，返回具体的原因说明
     *
     * 响应状态码说明：
     * - 200: 表示系统正常，所有服务运行良好
     * - 503: 表示服务不可用，可能是由于系统故障或维护中
     *
     * @return 返回一个Call对象，该对象包含Health类型的健康状态信息
     *         Health类包含了系统的健康状态数据。
     */
    @GET("/health")
    fun getHealth(): Call<Health>
}