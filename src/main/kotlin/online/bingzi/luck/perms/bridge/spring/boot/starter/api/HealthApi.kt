package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.health.Health
import retrofit2.Call
import retrofit2.http.GET

/**
 * 健康检查接口
 * 用于检查LuckPerms系统的运行状态
 */
interface HealthApi {
    /**
     * 获取系统健康状态
     * 
     * 此接口会返回：
     * 1. 系统整体健康状态
     * 2. 存储系统连接状态
     * 3. 存储系统响应时间
     * 4. 如果存在问题，会返回具体原因
     *
     * 响应状态码：
     * - 200: 系统正常
     * - 503: 服务不可用
     *
     * @return 健康状态信息
     */
    @GET("/health")
    fun getHealth(): Call<Health>
} 