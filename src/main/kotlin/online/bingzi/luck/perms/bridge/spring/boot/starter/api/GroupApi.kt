package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.NewGroup
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.NewNode
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.PermissionCheckRequest
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.result.PermissionCheckResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/group")
class GroupApi(private val luckPerms: LuckPerms) {

    /**
     * 获取所有组
     */
    @GetMapping
    fun getAllGroups(): ResponseEntity<List<String>> {
        return try {
            val groups = luckPerms.groupManager.loadedGroups.map { it.name }
            ResponseEntity.ok(groups)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 创建新组
     */
    @PostMapping
    fun createGroup(@RequestBody newGroup: NewGroup): ResponseEntity<Any> {
        return try {
            val group = luckPerms.groupManager.createAndLoadGroup(newGroup.name)
                ?: return ResponseEntity.status(HttpStatus.CONFLICT).build()
            ResponseEntity.status(HttpStatus.CREATED).body(group)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * 获取指定组信息
     */
    @GetMapping("/{groupName}")
    fun getGroup(@PathVariable groupName: String): ResponseEntity<Any> {
        return try {
            val group = luckPerms.groupManager.getGroup(groupName)
                ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(group)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 删除指定组
     */
    @DeleteMapping("/{groupName}")
    fun deleteGroup(@PathVariable groupName: String): ResponseEntity<Any> {
        return try {
            val group = luckPerms.groupManager.getGroup(groupName)
                ?: return ResponseEntity.notFound().build()
            luckPerms.groupManager.deleteGroup(group)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 获取组权限节点
     */
    @GetMapping("/{groupName}/nodes")
    fun getGroupNodes(@PathVariable groupName: String): ResponseEntity<Collection<Node>> {
        return try {
            val group = luckPerms.groupManager.getGroup(groupName)
                ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(group.nodes)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 添加组权限节点
     */
    @PostMapping("/{groupName}/nodes")
    fun addGroupNode(
        @PathVariable groupName: String,
        @RequestBody node: NewNode
    ): ResponseEntity<Collection<Node>> {
        return try {
            val group = luckPerms.groupManager.getGroup(groupName)
                ?: return ResponseEntity.notFound().build()
            
            val newNode = Node.builder(node.key)
                .value(node.value)
                .context(node.context ?: emptyList())
                .expiry(node.expiry ?: 0)
                .build()
            
            group.data().add(newNode)
            luckPerms.groupManager.saveGroup(group)
            
            ResponseEntity.ok(group.nodes)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * 检查组权限
     */
    @GetMapping("/{groupName}/permission-check")
    fun checkGroupPermission(
        @PathVariable groupName: String,
        @RequestParam permission: String
    ): ResponseEntity<PermissionCheckResult> {
        return try {
            val group = luckPerms.groupManager.getGroup(groupName)
                ?: return ResponseEntity.notFound().build()
            
            val result = group.cachedData.permissionData.checkPermission(permission)
            ResponseEntity.ok(PermissionCheckResult(result.result().toString(), result.node()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * 使用自定义查询选项检查组权限
     */
    @PostMapping("/{groupName}/permission-check")
    fun checkGroupPermissionWithOptions(
        @PathVariable groupName: String,
        @RequestBody request: PermissionCheckRequest
    ): ResponseEntity<PermissionCheckResult> {
        return try {
            val group = luckPerms.groupManager.getGroup(groupName)
                ?: return ResponseEntity.notFound().build()
            
            val queryOptions = request.queryOptions?.toQueryOptions()
            val result = if (queryOptions != null) {
                group.cachedData.getPermissionData(queryOptions).checkPermission(request.permission)
            } else {
                group.cachedData.permissionData.checkPermission(request.permission)
            }
            
            ResponseEntity.ok(PermissionCheckResult(result.result().toString(), result.node()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
} 