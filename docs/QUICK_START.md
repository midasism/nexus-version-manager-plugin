# 快速开始指南

## 🚀 5分钟上手

### 第一步：在IDEA中打开项目

```bash
cd /Users/midasgao/code/distinct
open -a "IntelliJ IDEA" nexus-version-manager-plugin
```

或者在IDEA中：`File > Open` 选择 `nexus-version-manager-plugin` 目录

### 第二步：初始化Gradle Wrapper（首次运行）

在IDEA终端或命令行中执行：

```bash
cd nexus-version-manager-plugin

# 如果系统有gradle命令
gradle wrapper --gradle-version=8.5

# 或者手动下载wrapper jar
curl -o gradle/wrapper/gradle-wrapper.jar \
  https://repo1.maven.org/maven2/org/gradle/gradle-wrapper/8.5/gradle-wrapper-8.5.jar
```

### 第三步：运行测试IDEA

在IDEA中：
1. 打开右侧 `Gradle` 面板
2. 展开 `nexus-version-manager-plugin > Tasks > intellij`
3. 双击 `runIde`

或者命令行：
```bash
./gradlew runIde
```

### 第四步：配置插件

在启动的测试IDEA中：

1. `Settings > Tools > Nexus Version Manager`
2. 填写配置：
   ```
   Nexus服务器地址: http://maven.distinctclinic.com:8081/nexus
   认证Token: [从浏览器Cookie中获取]
   连接超时: 30
   ```
3. 点击 `测试连接` 验证
4. 点击 `OK` 保存

### 第五步：使用插件

1. 在测试IDEA中打开Maven项目（如 `distinct-base-data-project`）
2. 点击右侧工具栏的 `Nexus Versions` 图标
3. 选择一个模块（如 `distinct-base-data-api`）
4. 点击 `刷新` 按钮
5. 查看版本列表！

## 📋 获取认证Token的方法

### Chrome / Edge
1. 访问 http://maven.distinctclinic.com:8081/nexus
2. 登录你的账号
3. 按 `F12` 打开开发者工具
4. 切换到 `Application` 标签
5. 左侧选择 `Cookies > http://maven.distinctclinic.com:8081`
6. 找到 `NX-ANTI-CSRF-TOKEN`
7. 复制它的值（Value列）

### Firefox
1. 访问 http://maven.distinctclinic.com:8081/nexus
2. 登录你的账号
3. 按 `F12` 打开开发者工具
4. 切换到 `存储` 标签
5. 左侧选择 `Cookie > http://maven.distinctclinic.com:8081`
6. 找到 `NX-ANTI-CSRF-TOKEN`
7. 复制它的值

### Safari
1. 启用开发者菜单：`Safari > 偏好设置 > 高级 > 显示开发菜单`
2. 访问 http://maven.distinctclinic.com:8081/nexus
3. 登录你的账号
4. `开发 > 显示Web检查器`
5. 切换到 `存储` 标签
6. 选择 `Cookies`
7. 找到 `NX-ANTI-CSRF-TOKEN` 并复制值

## ✨ 核心功能演示

### 1. 查询版本列表
- 选择模块 → 点击刷新 → 查看版本列表
- 版本按倒序排列（最新在上）
- 区分快照版本和正式版本

### 2. 复制版本号
- **双击**版本号行 → 自动复制到剪贴板
- 粘贴到 `pom.xml` 中使用

### 3. 右键菜单
- **复制版本号**：只复制版本号（如 `2.5.6`）
- **复制完整坐标**：复制Maven坐标（如 `com.distinct:distinct-base-data-api:2.5.6`）
- **在Nexus中打开**：在浏览器中打开对应的Nexus页面

## 🔧 常见问题

### Q: 提示"未找到Maven模块"？
**A**: 确保：
1. 当前项目是Maven项目
2. pom.xml文件正确
3. IDEA已完成Maven项目同步

### Q: 查询返回空列表？
**A**: 检查：
1. artifactId是否在Nexus中存在
2. 认证Token是否有效
3. 网络是否连通
4. 查看IDEA日志是否有错误

### Q: 认证Token在哪里找？
**A**: 参考上面的"获取认证Token的方法"章节

## 📦 构建插件用于生产环境

当测试完成后，构建正式版本：

```bash
cd nexus-version-manager-plugin
./gradlew buildPlugin
```

插件zip文件位于：
```
build/distributions/nexus-version-manager-plugin-1.0.0.zip
```

安装到真实IDEA：
1. `Settings > Plugins`
2. 点击齿轮图标 ⚙️
3. 选择 `Install Plugin from Disk...`
4. 选择上面生成的zip文件
5. 重启IDEA

## 🎯 使用场景示例

### 场景1：查看API模块的最新版本
```
1. 打开 distinct-base-data-project
2. 工具窗口选择 distinct-base-data-api
3. 点击刷新
4. 看到最新版本 2.5.6
```

### 场景2：升级依赖版本
```
1. 在工具窗口查询到最新版本 2.5.7
2. 双击版本号复制
3. 打开其他项目的 pom.xml
4. 找到依赖：
   <dependency>
     <groupId>com.distinct</groupId>
     <artifactId>distinct-base-data-api</artifactId>
     <version>2.5.6</version>  <!-- 旧版本 -->
   </dependency>
5. 粘贴新版本号：
   <version>2.5.7</version>  <!-- 新版本 -->
```

### 场景3：快照版本转正式版本
```
1. 查询模块版本列表
2. 找到最新快照版本 2.5.7-SNAPSHOT
3. 记录版本号
4. 在IDEA中修改 pom.xml 版本为 2.5.7（去掉-SNAPSHOT）
5. 执行 Maven clean、install、deploy
6. 刷新插件查看新版本已发布
```

## 📚 更多文档

- [README.md](README.md) - 项目说明
- [TESTING.md](TESTING.md) - 详细测试指南
- [DEVELOPMENT.md](DEVELOPMENT.md) - 开发文档
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - 项目总结

## 💡 小贴士

1. **快捷键**：可以为工具窗口设置快捷键快速打开
2. **多模块**：支持同时查询多个模块，切换下拉框即可
3. **历史记录**：配置会自动保存，下次启动自动加载
4. **离线模式**：配置的Token长期有效，一次配置即可

## 🎉 开始使用吧！

现在你已经掌握了所有必要的知识，开始使用这个插件来提高你的工作效率吧！

有问题或建议？请联系：support@distinctclinic.com
