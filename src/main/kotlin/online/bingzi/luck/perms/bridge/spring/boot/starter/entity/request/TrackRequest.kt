package online.bingzi.luck.perms.bridge.spring.boot.starter.entity.request

import online.bingzi.luck.perms.bridge.spring.boot.starter.entity.Context

/**
 * 轨道操作请求类
 *
 * 该类用于封装轨道操作请求的信息，包括轨道名称和上下文集合。
 * 主要用于传递与轨道相关的请求数据，便于在服务层进行处理。
 *
 * @property track 轨道名称，字符串类型，表示具体的轨道标识。
 * @property context 上下文集合，列表类型，可选参数，包含与操作相关的上下文信息。
 * 如果没有提供，则默认为 null。
 */
data class TrackRequest(
    val track: String, // 轨道名称，必须提供
    val context: List<Context>? = null // 上下文集合，默认为 null，表示没有任何上下文
) 