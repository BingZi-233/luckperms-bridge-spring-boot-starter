# 更新日志

本项目遵循 [语义化版本 2.0.0](https://semver.org/lang/zh-CN/) 规范。

## [未发布]

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

### 🔧 优化
- 实体类系统优化
  - 遵循SOLID原则拆分实体类
  - 添加详细的中文注释文档
  - 规范化字段验证注解
- API接口优化
  - 重构API接口实现,统一使用Retrofit2风格
  - 移除Spring MVC实现,保持接口单一职责
  - 完善API接口文档注释

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

### ⚡️ 性能优化
- 无

### 🔨 重构
- 无

### ⚠️ 破坏性变更
- 无

## [0.0.1] - 2024-03-xx

### 重构
- 重构了result包下的类结构,遵循单一职责原则(SRP)
  - 拆分 `PlayerSaveResult.kt` 为 `PlayerSaveResult.kt` 和 `PlayerSaveResultOutcome.kt`
  - 拆分 `PromotionResult.kt` 为 `PromotionResult.kt` 和 `PromotionStatus.kt` 
  - 拆分 `DemotionResult.kt` 为 `DemotionResult.kt` 和 `DemotionStatus.kt`

## 版本号说明

- 主版本号：当做了不兼容的 API 修改
- 次版本号：当做了向下兼容的功能性新增
- 修订号：当做了向下兼容的问题修正 