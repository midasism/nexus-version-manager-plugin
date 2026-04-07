# 开发文档

## 项目结构

```
nexus-version-manager-plugin/
├── build.gradle.kts              # Gradle构建配置
├── gradle.properties             # 构建属性配置
├── settings.gradle.kts          # Gradle项目设置
├── README.md                     # 项目说明
├── TESTING.md                    # 测试指南
├── DEVELOPMENT.md               # 开发文档
└── src/main/
    ├── kotlin/com/distinct/nexus/
    │   ├── models/               # 数据模型
    │   │   ├── MavenModule.kt   # Maven模块信息
    │   │   ├── MavenVersion.kt  # Maven版本信息
    │   │   └── NexusConfig.kt   # Nexus配置
    │   ├── services/             # 服务层
    │   │   ├── NexusApiService.kt        # Nexus API调用
    │   │   ├── NexusSettingsState.kt     # 设置持久化
    │   │   ├── ProjectInfoService.kt     # 项目信息读取
    │   │   ├── VersionComparator.kt      # 版本比较器
    │   │   └── VersionParser.kt          # 版本解析工具
    │   └── ui/                   # UI组件
    │       ├── NexusSettingsConfigurable.kt  # 设置页面
    │       ├── NexusSettingsPanel.kt         # 设置面板UI
    │       ├── NexusVersionToolWindow.kt     # 工具窗口
    │       ├── NexusVersionToolWindowFactory.kt # 工具窗口工厂
    │       └── VersionTableModel.kt          # 表格模型
    └── resources/
        ├── META-INF/
        │   └── plugin.xml        # 插件配置
        └── icons/
            └── nexus.svg         # 插件图标

```

## 核心组件说明

### 1. 数据模型层 (models/)

#### MavenVersion
表示一个Maven版本的信息，包括版本号、是否快照版本、仓库、上传时间等。

#### MavenModule
表示一个Maven模块的信息，从pom.xml中提取groupId、artifactId、version等。

#### NexusConfig
Nexus服务器配置，包括服务器地址、认证Token、超时设置。

### 2. 服务层 (services/)

#### NexusApiService
- 负责与Nexus REST API通信
- 使用OkHttp发送HTTP请求
- 解析Nexus ExtDirect API的JSON响应
- 提供版本查询和连接测试功能

#### ProjectInfoService
- 读取Maven项目信息
- 使用IntelliJ Maven API获取项目模块
- 解析pom.xml文件提取Maven坐标

#### VersionComparator
- 实现Maven版本号比较逻辑
- 正式版本优先于快照版本
- 使用语义化版本号比较规则

#### VersionParser
- 版本号解析和转换工具
- 提供版本递增、快照/正式版本转换等功能

#### NexusSettingsState
- 使用IntelliJ持久化API保存配置
- 配置文件存储在 `~/.config/JetBrains/*/nexus-version-manager.xml`

### 3. UI层 (ui/)

#### NexusVersionToolWindow
- 主工具窗口实现
- 包含模块选择器、刷新按钮、版本表格
- 实现双击复制、右键菜单等交互

#### NexusSettingsConfigurable
- Settings页面集成
- 实现Configurable接口
- 管理配置的加载、修改、保存

## 关键技术点

### 1. IntelliJ Platform API使用

#### Service注册
```xml
<extensions defaultExtensionNs="com.intellij">
    <applicationService serviceImplementation="..."/>
    <projectService serviceImplementation="..."/>
</extensions>
```

#### 获取Service实例
```kotlin
// Application级别Service
val service = ApplicationManager.getApplication()
    .getService(NexusApiService::class.java)

// Project级别Service
val service = project.getService(ProjectInfoService::class.java)
```

### 2. 异步任务处理

使用 `ProgressManager` 在后台线程执行耗时操作：

```kotlin
ProgressManager.getInstance().run(object : Task.Backgroundable(project, "任务名称", true) {
    override fun run(indicator: ProgressIndicator) {
        // 后台任务逻辑
        ApplicationManager.getApplication().invokeLater {
            // UI更新
        }
    }
})
```

### 3. Nexus API调用

Nexus 3使用ExtDirect RPC API：

**请求格式**：
```json
{
  "action": "coreui_Search",
  "method": "read",
  "data": [{
    "filter": [
      {"property": "keyword", "value": "artifactId"},
      {"property": "name.raw", "value": "artifactId"}
    ],
    "sort": [{"property": "version", "direction": "DESC"}]
  }],
  "type": "rpc"
}
```

**响应格式**：
```json
{
  "result": {
    "data": [
      {
        "version": "2.5.6",
        "repository": "releases",
        "lastModified": 1234567890
      }
    ]
  }
}
```

### 4. 版本比较算法

版本号比较规则：
1. 正式版本 > 快照版本
2. 版本号按数字部分比较（如 2.5.6 > 2.5.5）
3. 同数字时按字符串比较（如 2.5.6-alpha < 2.5.6-beta）

## 版本兼容性

### 支持的IDEA版本
- 最低：IntelliJ IDEA 2023.1 (build 231)
- 最高：IntelliJ IDEA 2026.3 (build 263.*)

### 兼容性注意事项

1. **避免使用新API**
   - 不使用2023.2+引入的新API
   - 使用稳定的Java Swing组件而非新UI DSL

2. **Service注册方式**
   - 使用XML注册而非@Service注解
   - 兼容旧版本的获取方式

3. **Java版本**
   - 编译目标：Java 17
   - IDEA 2023.1+需要Java 17运行时

## 构建和发布

### 本地构建

```bash
./gradlew build
```

生成的插件文件位于：`build/distributions/nexus-version-manager-plugin-1.0.0.zip`

### 运行测试IDE

```bash
./gradlew runIde
```

### 验证插件

```bash
./gradlew verifyPlugin
```

检查插件配置和兼容性问题。

### 发布到JetBrains Marketplace

1. 在 https://plugins.jetbrains.com 注册账号
2. 创建新插件
3. 上传构建的zip文件
4. 等待审核通过

## 未来功能扩展

### 短期计划
- [ ] 版本号递增功能
- [ ] 直接更新pom.xml中的版本号
- [ ] 支持批量查询多个模块

### 中期计划
- [ ] 一键执行clean、install、deploy流程
- [ ] 版本发布历史记录
- [ ] 版本依赖关系可视化

### 长期计划
- [ ] 支持Gradle项目
- [ ] 支持其他Maven仓库（Artifactory、GitLab Maven Registry等）
- [ ] 版本发布工作流集成（Git tag、changelog生成等）

## 贡献指南

1. Fork项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 许可证

待定

## 联系方式

- 作者：Distinct Clinic Team
- Email：support@distinctclinic.com
