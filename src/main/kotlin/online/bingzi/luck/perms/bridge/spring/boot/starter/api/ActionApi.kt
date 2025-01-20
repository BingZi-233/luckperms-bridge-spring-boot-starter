package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import net.luckperms.api.LuckPerms
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Action
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 操作记录管理接口
 */
@RestController
@RequestMapping("/action")
class ActionApi(private val luckPerms: LuckPerms) {

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
     */
    @GetMapping
    fun getActions(
        @RequestParam(required = false) pageSize: Int?,
        @RequestParam(required = false) pageNumber: Int?,
        @RequestParam(required = false) source: String?,
        @RequestParam(required = false) user: String?,
        @RequestParam(required = false) group: String?,
        @RequestParam(required = false) track: String?,
        @RequestParam(required = false) search: String?
    ): ResponseEntity<Map<String, Any>> {
        return try {
            // 获取日志存储实例
            val logStore = luckPerms.actionLogger

            // 构建查询
            var query = logStore.queryBuilder()
            
            // 添加过滤条件
            source?.let { query = query.source(it) }
            user?.let { query = query.targetUser(it) }
            group?.let { query = query.targetGroup(it) }
            track?.let { query = query.targetTrack(it) }
            search?.let { query = query.searchContent(it) }
            
            // 添加分页
            if (pageSize != null && pageNumber != null) {
                query = query.page(pageNumber, pageSize)
            }
            
            // 执行查询
            val result = query.build().execute()
            
            // 构建响应
            val response = mapOf(
                "entries" to result.entries,
                "overallSize" to result.overallSize
            )
            
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * 提交新的操作记录
     */
    @PostMapping
    fun submitAction(@RequestBody action: Action): ResponseEntity<Any> {
        return try {
            // 获取日志存储实例
            val logStore = luckPerms.actionLogger
            
            // 构建操作记录
            val entry = logStore.createEntry()
                .timestamp(action.timestamp)
                .source(action.source.uniqueId, action.source.name)
                .target(action.target.uniqueId, action.target.name, action.target.type)
                .description(action.description)
                .build()
            
            // 提交记录
            logStore.submit(entry)
            
            ResponseEntity.accepted().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
} 