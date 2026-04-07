# 测试指南

## 准备工作

确保你的开发环境满足以下要求：

- JDK 17或更高版本
- IntelliJ IDEA 2023.1或更高版本
- Gradle 8.5或更高版本

## 方法一：使用IntelliJ IDEA（推荐）

### 1. 在IDEA中打开项目

```bash
# 使用IDEA打开项目目录
open -a "IntelliJ IDEA" nexus-version-manager-plugin
```

或者通过IDEA菜单：File > Open，选择 `nexus-version-manager-plugin` 目录。

### 2. 等待Gradle同步

IDEA会自动识别这是一个Gradle项目并开始同步依赖。等待同步完成。

### 3. 运行插件

在IDEA右侧的Gradle面板中：

1. 展开 `nexus-version-manager-plugin > Tasks > intellij`
2. 双击 `runIde` 任务

这将启动一个带有插件的IDEA实例。

### 4. 测试插件功能

在启动的测试IDEA中：

#### 4.1 配置Nexus连接

1. 打开 `Settings > Tools > Nexus Version Manager`
2. 配置：
   - Nexus服务器地址：`http://maven.distinctclinic.com:8081/nexus`
   - 认证Token：从浏览器Cookie中获取 `NX-ANTI-CSRF-TOKEN` 的值
   - 连接超时：30秒
3. 点击"测试连接"验证配置
4. 点击"Apply"保存配置

#### 4.2 打开Maven项目

在测试IDEA中打开一个Maven项目，例如：
- `/Users/midasgao/code/distinct/distinct-base-data-project`

#### 4.3 使用工具窗口

1. 点击右侧工具栏的 "Nexus Versions" 图标
2. 在下拉框中选择一个模块（如 `distinct-base-data-api`）
3. 点击"刷新"按钮
4. 查看版本列表，应该能看到：
   - 版本号按倒序排列
   - 正式版本在前，快照版本在后
   - 显示类型（快照/正式）、仓库、上传时间

#### 4.4 测试交互功能

- **双击版本号**：版本号会被复制到剪贴板
- **右键菜单**：
  - 复制版本号
  - 复制完整坐标（groupId:artifactId:version）
  - 在Nexus中打开

## 方法二：使用命令行

### 1. 下载Gradle Wrapper Jar

由于我们需要完整的gradle-wrapper.jar，请先下载：

```bash
cd nexus-version-manager-plugin
curl -o gradle/wrapper/gradle-wrapper.jar \
  https://raw.githubusercontent.com/gradle/gradle/v8.5.0/gradle/wrapper/gradle-wrapper.jar
```

或者使用系统已安装的Gradle初始化wrapper：

```bash
cd nexus-version-manager-plugin
gradle wrapper --gradle-version=8.5
```

### 2. 构建插件

```bash
./gradlew build
```

### 3. 运行测试IDE

```bash
./gradlew runIde
```

## 验证清单

- [ ] 插件能在IDEA 2023.1+版本中加载
- [ ] 配置页面能正常打开和保存设置
- [ ] 能成功连接到Nexus服务器
- [ ] 能识别Maven项目的模块
- [ ] 能查询并显示版本列表
- [ ] 版本按正确顺序排列（正式版在前，版本号倒序）
- [ ] 双击复制版本号功能正常
- [ ] 右键菜单功能正常
- [ ] 在浏览器中打开Nexus功能正常

## 常见问题

### Q: 查询版本时返回空列表

A: 检查以下几点：
1. Nexus服务器地址是否正确
2. 认证Token是否有效
3. artifactId是否在Nexus中存在
4. 网络连接是否正常

### Q: 无法连接到Nexus服务器

A: 
1. 确认服务器地址可访问
2. 检查网络防火墙设置
3. 验证认证Token是否正确

### Q: 工具窗口不显示

A: 
1. 检查是否在Maven项目中
2. 尝试重新打开IDEA
3. 查看IDEA日志是否有错误信息

## 下一步

测试通过后，可以：

1. 使用 `./gradlew buildPlugin` 构建插件zip包
2. 在 `build/distributions/` 目录找到生成的插件
3. 通过 `Settings > Plugins > Install Plugin from Disk` 安装到真实IDEA中
