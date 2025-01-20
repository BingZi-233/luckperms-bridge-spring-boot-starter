package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Context

/**
 * 轨道操作请求
 *
 * @property track 轨道名称
 * @property context 上下文集合
 */
data class TrackRequest(
    val track: String,
    val context: List<Context>? = null
) 