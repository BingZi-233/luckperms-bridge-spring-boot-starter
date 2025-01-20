package online.bingzi.luck.perms.bridge.spring.boot.starter.api

import net.luckperms.api.LuckPerms
import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request.NewTrack
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 轨道管理接口
 */
@RestController
@RequestMapping("/track")
class TrackApi(private val luckPerms: LuckPerms) {

    /**
     * 获取所有轨道
     */
    @GetMapping
    fun getAllTracks(): ResponseEntity<List<String>> {
        return try {
            val tracks = luckPerms.trackManager.loadedTracks.map { it.name }
            ResponseEntity.ok(tracks)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 创建新轨道
     */
    @PostMapping
    fun createTrack(@RequestBody newTrack: NewTrack): ResponseEntity<Any> {
        return try {
            val track = luckPerms.trackManager.createAndLoadTrack(newTrack.name)
                ?: return ResponseEntity.status(HttpStatus.CONFLICT).build()
            ResponseEntity.status(HttpStatus.CREATED).body(track)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * 获取指定轨道信息
     */
    @GetMapping("/{trackName}")
    fun getTrack(@PathVariable trackName: String): ResponseEntity<Any> {
        return try {
            val track = luckPerms.trackManager.getTrack(trackName)
                ?: return ResponseEntity.notFound().build()
            ResponseEntity.ok(track)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 更新轨道
     */
    @PatchMapping("/{trackName}")
    fun updateTrack(
        @PathVariable trackName: String,
        @RequestBody groups: List<String>
    ): ResponseEntity<Any> {
        return try {
            val track = luckPerms.trackManager.getTrack(trackName)
                ?: return ResponseEntity.notFound().build()
            
            // 清除现有组
            track.clearGroups()
            // 添加新组
            groups.forEach { track.appendGroup(it) }
            
            // 保存更改
            luckPerms.trackManager.saveTrack(track)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * 删除轨道
     */
    @DeleteMapping("/{trackName}")
    fun deleteTrack(@PathVariable trackName: String): ResponseEntity<Any> {
        return try {
            val track = luckPerms.trackManager.getTrack(trackName)
                ?: return ResponseEntity.notFound().build()
            luckPerms.trackManager.deleteTrack(track)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
} 