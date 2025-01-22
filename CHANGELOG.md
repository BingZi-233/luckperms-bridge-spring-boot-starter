# 更新日志

本项目遵循 [语义化版本 2.0.0](https://semver.org/lang/zh-CN/) 规范。

## [1.0.12]

### 优化
- 优化了 `@EnableLuckPermsBridge` 注解的实现机制
  - 移除了注解上的条件判断，使其完全忽略配置文件
  - 调整了自动配置类的条件判断逻辑
  - 更新了注解文档，明确说明优先级规则
  - 修复了与 Spring Boot Web 依赖的兼容性问题

## [1.0.11]

### 优化
- 优化了 `@EnableLuckPermsBridge` 注解和配置属性的关系
  - 调整了注解的优先级，使其高于配置文件
  - 移除了 `LuckPermsAutoConfiguration` 中的条件注解
  - 更新了注解文档，说明了优先级规则

## [1.0.10]

### 优化
- 优化了 `EventSourceFactory` 的事件处理机制
  - 自动忽略 SSE 的 ping 心跳事件
  - 移除了对未知事件类型的警告日志
  - 更新了事件处理文档说明

## [1.0.9]

### 修复
- 修复了 `ConnectionStateHandler` 中的循环依赖问题
  - 使用异步方式发布事件，避免 Spring 容器初始化期间的循环依赖
  - 优化了生命周期管理，确保在容器完全初始化后才开始处理事件
  - 改进了线程池的关闭逻辑，确保资源正确释放

## [1.0.8]

### 修复
- 修复了 `ConnectionStateHandler` 中的类型转换问题
  - 将 `TaskExecutor` 改为 `ThreadPoolTaskExecutor`
  - 移除了不必要的类型检查
  - 优化了线程池的关闭逻辑

## [1.0.7]

### 修复
- 修复了 `ConnectionStateHandler` 中的注解导入问题
  - 将 `javax.annotation` 替换为 `jakarta.annotation`
  - 适配 Spring Boot 3.x 的 Jakarta EE 支持
  - 更新 `@PostConstruct` 和 `@PreDestroy` 注解的导入

## [1.0.6]

### 优化
- 优化了 `ConnectionStateHandler` 的事件发布机制
  - 移除了异步事件发布，避免循环依赖问题
  - 改进了生命周期管理，添加 `@PostConstruct` 和 `@PreDestroy` 支持
  - 优化了错误处理和日志记录
  - 修复了 Spring 容器初始化过程中的线程安全问题

## [1.0.5]

### 优化
- 优化了 `ConnectionStateHandler` 的生命周期管理
- 改进了事件发布机制，使用异步方式处理事件
- 优化了系统关闭时的日志输出
- 修复了启动和关闭过程中的循环依赖问题

## [1.0.4]

### 修复
- 修复了健康检查服务的线程安全问题
  - 使用 AtomicReference 确保状态更新的原子性
  - 优化状态更新逻辑，避免并发修改问题
- 优化了健康检查服务的调度配置
  - 修改 @Scheduled 注解配置方式
  - 支持通过配置文件设置检查周期
- 修复了配置属性注入问题
  - 优化配置类的组织结构
  - 修复重复的 Bean 定义问题

## [1.0.3]

### 新增
- 添加基于Spring-Retry的SSE连接重试机制
- 新增重试相关配置参数支持
  - `luck-perms.retry.initial-interval`: 初始重试间隔
  - `luck-perms.retry.multiplier`: 重试间隔倍数
  - `luck-perms.retry.max-interval`: 最大重试间隔
  - `luck-perms.retry.max-attempts`: 最大重试次数

### 优化
- 重构配置结构，统一到LuckPermsAutoConfiguration
- 完善配置元数据，提供更好的IDE支持
- 优化SSE连接的生命周期管理

### 文档
- 添加重试配置相关文档
- 更新配置示例和最佳实践

### 重构
- 将 `LuckPermsEvent.kt` 中的枚举类拆分为独立文件，遵循单一职责原则
  - 创建 `event/type/EventType.kt` 文件，独立管理事件类型枚举
  - 创建 `event/priority/EventPriority.kt` 文件，独立管理事件优先级枚举
  - 优化了主事件类的结构和导入关系
- 将 `ConnectionStateEvent.kt` 中的枚举类拆分为独立文件
  - 创建 `event/model/state/ConnectionState.kt` 文件，独立管理连接状态枚举
  - 优化了事件类的结构和导入关系
- 修复事件模型类的导入引用问题
  - 更新所有事件类的导入路径，使用新的枚举类位置
  - 统一导入语句的组织结构
  - 优化了代码的可维护性
- 修复事件工厂中的事件创建问题
  - 更新事件参数名称以匹配新的事件类结构
  - 统一使用 sourceType 作为来源标识
  - 简化了事件参数结构
- 修复事件系统中的循环依赖问题
  - 将 `ConnectionStateHandler` 标记为 Spring 组件
  - 优化依赖注入方式，使用构造器注入
  - 重构事件工厂的依赖管理

### 修复
- 修复了健康检查服务的线程安全问题
- 优化了健康检查服务的调度配置
- 修复了配置属性注入问题

## [1.0.2]

### 新增
- 添加 API 接口的自动配置
  - 添加 `UserApi` Bean 配置
  - 添加 `MessagingApi` Bean 配置
  - 添加 `HealthApi` Bean 配置
  - 所有 API Bean 都使用 `@ConditionalOnMissingBean` 注解，支持自定义覆盖
- 添加了健康检查功能
- 添加了重试机制
- 添加了事件监听机制

### 修复
- 修复 `PreNetworkSyncEvent` 和 `PostNetworkSyncEvent` 中的属性命名冲突问题
  - 将 `type` 属性重命名为 `syncType`，以避免与 `LuckPermsEvent` 接口的 `getType()` 方法冲突
  - 保持 JSON 序列化兼容性，继续使用 "type" 作为 JSON 字段名
- 更新 `EventListener` 中的相关引用，使用新的属性名 `syncType`
- 修复 Jackson 反序列化问题
  - 为 `PermissionCheckResult` 添加 `@JsonCreator` 和 `@JsonProperty` 注解
  - 为 `Node` 类添加 Jackson 序列化支持
  - 为 `Context` 类添加 Jackson 序列化支持
  - 为 `NodeType` 枚举添加 JSON 序列化和反序列化支持
  - 优化枚举值的大小写处理
- 修复多权限检查问题
  - 完善 `LuckPermsPermissionService` 的权限检查实现
  - 实现 `hasAllPermissions` 和 `hasAnyPermission` 方法
  - 使用 `checkUserPermissionWithOptions` API 进行权限检查
  - 支持 `@RequirePermission` 注解的 ALL/ANY 模式
  - 修复 `QueryOptions` 的 contexts 参数缺少默认值的问题

## [1.0.1]

### 新增
- 添加 `GroupSearchResult` 数据类，用于表示组搜索结果
- 完善 API 接口文档
- 添加 Spring Web 支持，用于请求上下文处理
- 添加了基础的权限检查功能
- 添加了用户组管理功能
- 添加了上下文支持

### 优化
- 重构权限检查相关实体类，提升类型安全性
- 优化依赖管理，按功能分类并添加注释
- 规范化查询选项和标志的枚举定义

### 修复
- 修复 `GroupApi` 中未解析的引用问题
- 修复 `HeaderUserIdentityService` 缺少 Web 依赖的问题
- 修复 `LuckPermsPermissionService` 中方法调用错误

## [1.0.0]

### 新增
- 项目初始化
- 基础框架搭建
- 基础权限检查功能
- 组权限管理功能
- 用户权限管理功能

### ✨ 新特性
- 项目初始化
- 完成注解系统设计文档
  - 设计核心注解系统
  - 设计元数据注解系统
  - 设计工具注解系统
  - 定义枚举类型
  - 设计配置系统
- 实现实体类系统
  - 实现权限节点相关实体(Node、NodeType)
  - 实现权限上下文实体(Context)
  - 实现权限组实体(Group)
  - 实现用户实体(User)
  - 实现权限轨道实体(Track)
  - 实现操作记录相关实体(Action、ActionSource、ActionTarget、ActionTargetType)
  - 实现元数据实体(Metadata)
- 选定技术栈
  - 选用Retrofit2(2.11.0)作为HTTP客户端实现LuckPerms API调用
- 实现API接口系统
  - 实现用户管理接口(UserApi)
  - 实现组管理接口(GroupApi)
    - 基础CRUD操作
    - 权限节点管理
    - 权限检查功能
  - 实现轨道管理接口(TrackApi)
    - 基础CRUD操作
    - 轨道组管理功能
  - 实现操作记录接口(ActionApi)
    - 操作记录查询功能
    - 操作记录提交功能
  - 实现消息服务接口(MessagingApi)
    - 推送更新消息功能
    - 推送用户更新功能
    - 发送自定义消息功能
- 实现@RequirePermission注解系统
  - 实现权限检查注解
  - 实现权限服务接口和实现
  - 实现用户身份服务接口和实现
  - 实现权限检查切面处理器
- 实现@RequireGroup注解系统
  - 实现组权限检查注解
  - 实现组服务接口和实现
  - 实现组检查切面处理器
  - 复用已有的设计模式和服务架构
- 实现@WithContext注解系统
  - 实现上下文要求注解
  - 实现上下文服务接口和实现
  - 实现上下文检查切面处理器
  - 支持重复注解实现多上下文
  - 与权限检查注解协同工作
- 实现@EnableLuckPermsBridge注解
  - 实现启用注解
  - 优化自动配置机制
  - 简化使用方式
  - 提供清晰的使用文档

### 🔧 优化
- 实体类系统优化
  - 遵循SOLID原则拆分实体类
  - 添加详细的中文注释文档
  - 规范化字段验证注解
- API接口优化
  - 重构API接口实现,统一使用Retrofit2风格
  - 移除Spring MVC实现,保持接口单一职责
  - 完善API接口文档注释
- 注解系统优化
  - 遵循SOLID原则设计接口和实现
  - 分离权限检查和用户身份获取逻辑
  - 实现灵活的权限检查模式(ANY/ALL)
  - 添加详细的中文注释文档
- 代码复用优化
  - 复用CheckMode枚举
  - 复用UserIdentityService
  - 保持一致的异常处理机制
  - 统一的注解处理模式
- 切面处理优化
  - 优化切面执行顺序
  - 实现注解组合处理
  - 支持多上下文条件
  - 增强代码可读性
- 配置机制优化
  - 移除spring.factories配置
  - 使用@Import导入配置
  - 简化启用流程
  - 提高代码可维护性
- Bean配置优化
  - 优化UserIdentityService的注入逻辑
  - 添加Bean之间的依赖关系约束
  - 修复DefaultUserIdentityService的条件注解
  - 完善自动配置的Bean注册顺序
  - 简化Bean配置，移除不必要的@ConditionalOnBean注解

### 🐛 修复
- 无

### 📝 文档
- 添加注解系统设计文档
- 添加项目进度跟踪文档
- 添加更新日志文档
- 更新实体类系统完成进度
- 更新API接口实现进度
  - 用户管理接口完成记录
  - 组管理接口完成记录
  - 轨道管理接口完成记录
  - 操作记录接口完成记录
- 更新项目进度文档
  - 添加注解系统实现进度
  - 更新下一步计划
- 更新更新日志
  - 记录注解系统实现细节
  - 记录代码优化内容
- 添加使用文档
  - 添加注解使用示例
  - 添加配置说明
  - 添加快速开始指南

### ⚡️ 性能优化
- 无

### 🔨 重构
- 无

### ⚠️ 破坏性变更
- 无

## 版本号说明

- 主版本号：当做了不兼容的 API 修改
- 次版本号：当做了向下兼容的功能性新增
- 修订号：当做了向下兼容的问题修正 

## [未发布]

### 新增
- 添加健康检查功能
  - 通过 LuckPerms API 定期检查系统健康状态
  - 支持配置检查周期和超时时间
  - 记录系统响应时间和停机时间
  - 提供详细的健康状态信息（存储系统连接状态、响应时间等）
- 增强配置提示功能
  - 添加健康检查相关配置的智能提示
  - 为配置项添加详细的说明文档
  - 提供场景化的配置建议
  - 支持配置值的快速选择

### 配置项
```yaml
luck-perms:
  health-check:
    enabled: true                # 是否启用健康检查
    period: PT30S               # 检查周期（30秒）
    timeout: PT5S               # 超时时间（5秒）
```

## [0.0.4] - 事件配置优化

### 新增
- 添加 `EventConfiguration` 配置类
  - 统一管理事件相关的Bean配置
  - 支持条件化Bean创建
  - 提供Bean覆盖能力

### 优化
- 改进事件相关Bean的管理方式
  - 从注解驱动改为配置驱动
  - 优化Bean之间的依赖关系
  - 提供更灵活的定制能力

### 变更
- 移除事件相关类上的 `@Component` 注解
- 通过配置类统一管理Bean的创建和注入

## [0.0.3] - 事件系统Spring集成

### 变更
- 重构事件系统，使用Spring的事件机制
  - 事件类继承自 `ApplicationEvent`
  - 使用 `ApplicationEventPublisher` 发布事件
  - 使用 `@EventListener` 注解监听事件
- 优化事件处理流程
  - 移除自定义事件总线
  - 移除自定义事件处理器
  - 简化事件发布和监听方式

### 移除
- 移除 `EventBus` 类，使用Spring的事件发布机制
- 移除 `EventHandler` 接口，使用Spring的事件监听机制

## [0.0.2] - 事件系统优化

### 优化
- 重构事件模型结构，严格遵循单一职责原则
  - 将事件类拆分为独立文件
  - 优化事件类的命名和组织
  - 完善事件类的文档注释
- 改进代码组织结构
  - 优化包结构，使其更加清晰
  - 删除冗余和重复的代码文件

### 移除
- 移除 `Events.kt`、`NetworkSyncEvents.kt` 和 `SyncEvents.kt` 合并文件
- 移除重复的事件定义文件

## [0.0.1] - 事件系统重构

### 新增
- 新增 `EventBus` 事件总线，用于统一管理事件分发
- 新增 `EventSourceFactory` 用于创建和管理事件监听器
- 新增事件优先级机制
- 新增事件类型枚举，增强类型安全

### 变更
- 重构事件处理系统，采用观察者模式
- 优化事件处理器接口，支持泛型和类型安全
- 改进事件生命周期管理

### 移除
- 移除旧的 `EventListener` 实现，改用 `EventSourceFactory` 