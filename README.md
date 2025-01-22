# LuckPerms Bridge Spring Boot Starter

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![GitHub release](https://img.shields.io/github/release/BingZi-233/luckperms-bridge-spring-boot-starter.svg)](../../releases)
[![Maven Central](https://img.shields.io/maven-central/v/online.bingzi/luckperms-bridge-spring-boot-starter.svg)](https://search.maven.org/artifact/online.bingzi/luckperms-bridge-spring-boot-starter)

LuckPerms Bridge Spring Boot Starter 是一个用于简化 LuckPerms API 集成的 Spring Boot Starter。它提供了一套简单的注解和配置方式，让你能够轻松地在 Spring Boot 项目中使用 LuckPerms 的功能。

## 项目状态

- [项目进度](PROGRESS.md) - 查看当前开发进度和计划
- [更新日志](CHANGELOG.md) - 查看版本更新历史

## 特性

- 🚀 开箱即用：最小化配置，快速集成
- 🎯 注解驱动：通过注解轻松实现权限控制
- 🔌 自动配置：自动配置所需的Bean和服务
- 🛠 完整API：支持所有LuckPerms REST API功能
- 📦 轻量级：最小化依赖，不引入额外负担

## 快速开始

### Maven

```xml
<dependency>
    <groupId>online.bingzi</groupId>
    <artifactId>luckperms-bridge-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```kotlin
implementation("online.bingzi:luckperms-bridge-spring-boot-starter:1.0.0")
```

### 基础配置

在你的`application.yml`或`application.properties`中添加以下配置：

```yaml
luck-perms:
  base-url: http://your-luckperms-server:8080  # LuckPerms API服务地址
  api-key: your-api-key                        # LuckPerms API密钥
  enabled: true                                # 是否启用（可选，默认true）
```

### 启用Starter

在你的Spring Boot主类上添加`@SpringBootApplication`注解：

```kotlin
@SpringBootApplication
class YourApplication

fun main(args: Array<String>) {
    runApplication<YourApplication>(*args)
}
```

## 配置说明

### 必需配置

- `luck-perms.base-url`: LuckPerms API服务的基础URL
  - 类型：String
  - 默认值：http://localhost:8080
  - 说明：指定LuckPerms REST API的访问地址

- `luck-perms.api-key`: LuckPerms API密钥
  - 类型：String
  - 默认值：""
  - 说明：用于API认证的密钥，可以在LuckPerms配置文件中找到

### 可选配置

- `luck-perms.enabled`: 是否启用LuckPerms Bridge
  - 类型：Boolean
  - 默认值：true
  - 说明：可以通过此配置快速启用/禁用所有功能

### 健康检查配置

- `luck-perms.health-check.enabled`: 是否启用健康检查
  - 类型：Boolean
  - 默认值：true
  - 说明：启用后将定期检查LuckPerms API的可用性

- `luck-perms.health-check.period`: 健康检查周期
  - 类型：Duration
  - 默认值：30s
  - 说明：指定多久执行一次健康检查，支持的时间单位：s（秒）、m（分钟）、h（小时）
  - 推荐值：
    - 10s：适用于对系统健康状态要求较高的场景
    - 30s：默认值，适合大多数场景
    - 1m：适用于对系统资源敏感的场景
    - 5m：适用于对实时性要求不高的场景

- `luck-perms.health-check.timeout`: 健康检查超时时间
  - 类型：Duration
  - 默认值：5s
  - 说明：单次检查的最大等待时间，超过此时间视为检查失败
  - 推荐值：
    - 3s：适用于本地服务或网络状况良好的环境
    - 5s：默认值，适合大多数场景
    - 10s：适用于网络状况不稳定的环境

### 重试配置
```yaml
luck-perms:
  retry:
    # 初始重试间隔（毫秒）
    initial-interval: 1000
    # 重试间隔倍数
    multiplier: 2.0
    # 最大重试间隔（毫秒）
    max-interval: 30000
    # 最大重试次数
    max-attempts: 5
```

### 配置最佳实践

1. **环境隔离**
   ```yaml
   # application-dev.yml
   luck-perms:
     base-url: http://dev-server:8080
     api-key: dev-key

   # application-prod.yml
   luck-perms:
     base-url: http://prod-server:8080
     api-key: prod-key
   ```

2. **安全配置**
   - 不要在代码中硬编码`api-key`
   - 建议使用环境变量或配置中心
   ```yaml
   luck-perms:
     api-key: ${LUCKPERMS_API_KEY}
   ```

3. **条件化配置**
   ```yaml
   luck-perms:
     enabled: ${ENABLE_LUCKPERMS:true}
   ```

## 注解使用

> 🔍 查看 [完整注解使用示例](docs/ANNOTATIONS.md) 了解更多详细用法和最佳实践。

### @RequirePermission

用于方法级别的权限检查：

```kotlin
@RequirePermission("admin.user.view")
fun viewUserInfo(userId: String): UserInfo {
    // 只有具有admin.user.view权限的用户才能访问
    return userService.getUserInfo(userId)
}
```

### @RequireGroup

用于组权限检查：

```kotlin
@RequireGroup("admin")
fun adminOperation() {
    // 只有admin组的成员才能访问
}
```

### @WithContext

添加上下文条件：

```kotlin
@WithContext("server", "lobby")
@RequirePermission("command.kick")
fun kickPlayer(player: String) {
    // 只在lobby服务器上检查command.kick权限
}
```

## 自定义用户身份识别

默认情况下，starter 提供了两种用户身份识别实现：
1. `DefaultUserIdentityService`: 使用 Spring Security 的 Principal 获取用户身份
2. `HeaderUserIdentityService`: 从请求头中获取用户身份

如果这两种实现不满足您的需求，您可以自定义实现 `UserIdentityService` 接口：

```kotlin
@Service
class CustomUserIdentityService : UserIdentityService {
    override fun getUserIdentity(): String {
        // 示例：从ThreadLocal中获取用户身份
        return UserContext.getCurrentUser()
            ?: throw UnauthorizedException("用户未登录")
    }
}
```

或者使用 JWT Token 进行身份识别：

```kotlin
@Service
class JwtUserIdentityService(
    private val jwtUtil: JwtUtil
) : UserIdentityService {
    override fun getUserIdentity(): String {
        val request = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val token = request.request.getHeader("Authorization")
            ?.removePrefix("Bearer ")
            ?: throw UnauthorizedException("未提供Token")
            
        return jwtUtil.extractUsername(token)
            ?: throw UnauthorizedException("无效的Token")
    }
}
```

自定义实现类需要注册为 Spring Bean，starter 会自动使用您的实现替代默认实现。您也可以使用条件注解来控制实现类的启用条件：

```kotlin
@Service
@ConditionalOnProperty(name = ["app.auth.type"], havingValue = "jwt")
class JwtUserIdentityService(/*...*/) : UserIdentityService {
    // ...
}

@Service
@ConditionalOnProperty(name = ["app.auth.type"], havingValue = "session")
class SessionUserIdentityService(/*...*/) : UserIdentityService {
    // ...
}
```

## 常见问题

1. **配置未生效？**
   - 检查`@SpringBootApplication`注解是否添加
   - 确认配置前缀是否正确（luck-perms）
   - 验证配置文件格式是否正确

2. **API调用失败？**
   - 确认base-url是否正确
   - 验证api-key是否有效
   - 检查网络连接是否正常

3. **权限检查不生效？**
   - 确认AOP依赖是否引入
   - 检查方法是否通过代理调用
   - 验证权限表达式格式是否正确

## 贡献指南

欢迎提交Issue和Pull Request！在贡献代码前，请：

1. Fork本仓库
2. 创建你的特性分支
3. 提交变更
4. 推送到你的分支
5. 创建Pull Request

## 许可证

本项目采用MIT许可证 - 查看[LICENSE](LICENSE)文件了解详情。 