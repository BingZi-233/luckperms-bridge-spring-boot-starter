# 注解使用示例

本文档提供了 LuckPerms Bridge 中所有注解的详细使用示例。

## 目录

- [@RequirePermission](#requirepermission)
- [@RequireGroup](#requiregroup)
- [@WithContext](#withcontext)
- [@EnableLuckPermsBridge](#enableluckpermsbridge)

## @RequirePermission

权限检查注解，用于方法级别的权限控制。

### 基础用法

```kotlin
@RequirePermission("admin.user.view")
fun viewUserInfo(userId: String): UserInfo {
    return userService.getUserInfo(userId)
}
```

### 多权限检查

```kotlin
// 检查多个权限（全部需要满足）
@RequirePermission(["admin.user.edit", "admin.user.view"], mode = CheckMode.ALL)
fun editUserInfo(userId: String, info: UserInfo) {
    userService.updateUserInfo(userId, info)
}

// 检查多个权限（满足其中之一即可）
@RequirePermission(["admin.user.delete", "admin.super"], mode = CheckMode.ANY)
fun deleteUser(userId: String) {
    userService.deleteUser(userId)
}
```

### 通配符权限

```kotlin
// 支持通配符权限检查
@RequirePermission("admin.user.*")
fun manageUser(userId: String) {
    // 用户具有任何以admin.user.开头的权限都可以访问
}
```

### 权限继承

```kotlin
// 支持权限继承关系
@RequirePermission("admin.*")
fun adminOperation() {
    // 用户具有admin级别的任何权限都可以访问
}
```

### 与其他注解组合

```kotlin
@RequirePermission("server.command.kick")
@WithContext("server", "lobby")
fun kickPlayer(player: String) {
    // 在lobby服务器上检查kick权限
}
```

## @RequireGroup

组权限检查注解，用于基于用户组的权限控制。

### 基础用法

```kotlin
@RequireGroup("admin")
fun adminOnly() {
    // 只有admin组的成员可以访问
}
```

### 多组检查

```kotlin
// 要求同时属于多个组
@RequireGroup(["mod", "helper"], mode = CheckMode.ALL)
fun modAndHelper() {
    // 用户必须同时是mod和helper组的成员
}

// 属于任意一个组即可
@RequireGroup(["admin", "mod"], mode = CheckMode.ANY)
fun adminOrMod() {
    // 用户是admin或mod组的成员都可以访问
}
```

### 与上下文组合

```kotlin
@RequireGroup("mod")
@WithContext("server", "survival")
fun survivalMod() {
    // 只有在survival服务器上的mod组成员可以访问
}
```

### 权限和组的组合

```kotlin
@RequireGroup("admin")
@RequirePermission("system.maintenance")
fun maintainSystem() {
    // 需要同时是admin组成员且拥有system.maintenance权限
}
```

## @WithContext

上下文条件注解，用于指定权限检查的上下文环境。

### 基础用法

```kotlin
@WithContext("server", "lobby")
@RequirePermission("command.broadcast")
fun broadcastInLobby(message: String) {
    // 只在lobby服务器上检查broadcast权限
}
```

### 多上下文

```kotlin
// 使用多个@WithContext注解
@WithContext("server", "survival")
@WithContext("world", "world_nether")
@RequirePermission("portal.create")
fun createNetherPortal() {
    // 只在survival服务器的下界中检查portal.create权限
}

// 组合多个上下文条件
@WithContext(["server=survival", "world=world_nether"])
@RequirePermission("portal.create")
fun createNetherPortalAlt() {
    // 与上面的示例效果相同
}
```

### 动态上下文

```kotlin
@WithContext("server", "#{serverContext.getCurrentServer()}")
@RequirePermission("command.kick")
fun kickPlayerFromCurrentServer(player: String) {
    // 在当前服务器上检查kick权限
}
```

### 复杂场景

```kotlin
@WithContext("server", "survival")
@WithContext("world", "world_nether")
@RequireGroup("builder")
@RequirePermission(["build.nether", "portal.create"], mode = CheckMode.ALL)
fun createCustomNetherPortal() {
    // 需要在survival服务器的下界中
    // 同时是builder组成员
    // 且同时拥有build.nether和portal.create权限
}
```

## @EnableLuckPermsBridge

启用注解，用于启用LuckPerms Bridge的功能。

### 基础用法

```kotlin
@SpringBootApplication
@EnableLuckPermsBridge
class YourApplication

fun main(args: Array<String>) {
    runApplication<YourApplication>(*args)
}
```

### 条件启用

```kotlin
@SpringBootApplication
@EnableLuckPermsBridge
@ConditionalOnProperty(name = ["app.permissions.enabled"], havingValue = "true")
class YourApplication

fun main(args: Array<String>) {
    runApplication<YourApplication>(*args)
}
```

## 最佳实践

1. **权限粒度控制**
   - 使用细粒度的权限而不是粗粒度的权限
   - 合理使用权限继承关系
   - 避免过度使用通配符权限

2. **上下文使用**
   - 明确指定必要的上下文
   - 避免不必要的上下文检查
   - 使用动态上下文时注意性能影响

3. **组合使用**
   - 合理组合不同类型的注解
   - 注意注解的执行顺序
   - 避免过度组合导致的性能问题

4. **错误处理**
   - 为权限检查失败提供合适的错误处理
   - 使用自定义异常处理器
   - 提供友好的错误信息 