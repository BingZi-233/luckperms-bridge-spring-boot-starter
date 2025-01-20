# LuckPerms Bridge 注解系统设计

## 1. 核心注解

### @EnableLuckPermsBridge
- 功能：启用 LuckPerms Bridge 功能
- 使用位置：配置类
- 属性：
  - value: String - API地址（可选，默认从配置文件读取）
  - token: String - API令牌（可选，默认从配置文件读取）

### @RequirePermission
- 功能：权限检查注解
- 使用位置：方法、类
- 属性：
  - value: String[] - 需要的权限列表
  - mode: PermissionMode - 权限匹配模式（ALL/ANY）
  - action: FailAction - 权限检查失败时的动作

### @RequireGroup
- 功能：用户组检查注解
- 使用位置：方法、类
- 属性：
  - value: String[] - 需要的用户组列表
  - mode: GroupMode - 用户组匹配模式（ALL/ANY）
  - action: FailAction - 检查失败时的动作

### @WithContext
- 功能：上下文限定注解
- 使用位置：方法、类
- 属性：
  - server: String - 服务器上下文
  - world: String - 世界上下文
  - expiry: Duration - 上下文有效期

## 2. 元数据注解

### @MetaData
- 功能：元数据检查注解
- 使用位置：方法、类
- 属性：
  - key: String - 元数据键
  - value: String - 元数据值
  - mode: MetaMode - 匹配模式（EXACT/CONTAINS/PREFIX/SUFFIX）

### @PrefixRequirement
- 功能：前缀检查注解
- 使用位置：方法、类
- 属性：
  - value: String - 需要的前缀
  - weight: Int - 权重要求（可选）

### @SuffixRequirement
- 功能：后缀检查注解
- 使用位置：方法、类
- 属性：
  - value: String - 需要的后缀
  - weight: Int - 权重要求（可选）

## 3. 工具注解

### @LuckPermsUser
- 功能：注入当前用户的 LuckPerms 用户对象
- 使用位置：方法参数
- 属性：
  - required: Boolean - 是否必须（默认true）

### @GroupMember
- 功能：注入指定组的成员信息
- 使用位置：方法参数
- 属性：
  - value: String - 组名
  - includeInherited: Boolean - 是否包含继承的成员

## 4. 枚举定义

### PermissionMode
```kotlin
enum class PermissionMode {
    ALL,    // 需要所有权限
    ANY     // 任一权限即可
}
```

### GroupMode
```kotlin
enum class GroupMode {
    ALL,    // 需要所有用户组
    ANY     // 任一用户组即可
}
```

### FailAction
```kotlin
enum class FailAction {
    THROW_EXCEPTION,    // 抛出异常
    RETURN_FALSE,       // 返回false
    RETURN_NULL,        // 返回null
    CUSTOM             // 自定义处理
}
```

### MetaMode
```kotlin
enum class MetaMode {
    EXACT,      // 精确匹配
    CONTAINS,   // 包含匹配
    PREFIX,     // 前缀匹配
    SUFFIX      // 后缀匹配
}
```

## 5. 使用示例

```kotlin
@RestController
@RequestMapping("/api/admin")
@RequireGroup("admin")
class AdminController {

    @GetMapping("/users")
    @RequirePermission(["luckperms.users.list"])
    fun listUsers(@LuckPermsUser user: User): List<UserDTO> {
        // 实现逻辑
    }

    @PostMapping("/group/create")
    @RequirePermission(
        value = ["luckperms.group.create", "luckperms.group.manage"],
        mode = PermissionMode.ALL
    )
    @WithContext(server = "global")
    fun createGroup(@RequestBody request: CreateGroupRequest): GroupDTO {
        // 实现逻辑
    }
}
```

## 6. 注解处理流程

1. 请求进入系统
2. 注解切面处理器拦截请求
3. 收集类级别和方法级别的所有注解
4. 按优先级顺序处理注解：
   - Context > Group > Permission > Meta
5. 所有注解验证通过后执行目标方法
6. 异常处理和结果返回

## 7. 配置属性

```yaml
luckperms:
  bridge:
    api-url: "http://localhost:8080"
    token: "your-api-token"
    default-context:
      server: "global"
      world: "world"
    cache:
      enabled: true
      ttl: 300000  # 5分钟
    fail-fast: true  # 快速失败模式
``` 