# Nexus Version Manager Plugin

[![版本](https://img.shields.io/github/v/release/midasism/nexus-version-manager-plugin?style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/releases)
[![构建](https://img.shields.io/github/actions/workflow/status/midasism/nexus-version-manager-plugin/build.yml?branch=main&style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/actions)
[![许可证](https://img.shields.io/github/license/midasism/nexus-version-manager-plugin?style=flat-square)](LICENSE)
[![下载量](https://img.shields.io/github/downloads/midasism/nexus-version-manager-plugin/total?style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/releases)
[![Stars](https://img.shields.io/github/stars/midasism/nexus-version-manager-plugin?style=flat-square)](https://github.com/midasism/nexus-version-manager-plugin/stargazers)

[English](README.md) | 中文

IntelliJ IDEA插件，用于管理私有Nexus仓库中的Maven版本。

## 最新版本：v1.1.1 🎉

### 核心功能

- ✅ **自动识别API模块**：自动查询所有`-api`结尾的Maven模块
- ✅ **版本过滤**：支持全部/正式/快照版本切换
- ✅ **批量展示**：一次查询显示所有API模块的版本
- ✅ **版本排序**：按版本号倒序展示，正式版本优先
- ✅ **快速复制**：双击或右键复制版本号
- ✅ **Nexus集成**：在浏览器中打开对应的Nexus页面
- ✅ **配置持久化**：自动加载已保存的配置
- ✅ **自动刷新**：首次打开自动加载数据
- ✅ **时间准确**：正确显示版本上传时间

## 兼容性

- IntelliJ IDEA 2023.1 - 2026.x
- JDK 17+
- Maven 项目

## 开发

### 构建插件

```bash
./gradlew buildPlugin
```

### 运行开发环境

```bash
./gradlew runIde
```

### 安装

构建完成后，在 `build/distributions/` 目录下会生成插件zip包，可以通过IDEA的插件安装界面安装。

## 配置

Settings > Tools > Nexus Version Manager

- Nexus服务器地址
- 认证Token（从浏览器Cookie中获取NX-ANTI-CSRF-TOKEN）

## 快速开始

### 1. 配置Nexus连接

首次使用前需要配置：

```
Settings > Tools > Nexus Version Manager
- Nexus服务器地址: http://maven.distinctclinic.com:8081/nexus
- 认证Token: [从浏览器Cookie获取NX-ANTI-CSRF-TOKEN]
- 连接超时: 30秒
```

### 2. 使用插件

```
1. 打开Maven项目（包含-api结尾的模块）
2. 点击右侧工具栏的"Nexus Versions"图标
3. 点击"刷新所有API模块"按钮
4. 等待查询完成，查看版本列表
```

### 3. 版本过滤

在工具窗口中可以选择：
- **全部版本**：显示所有版本
- **仅正式版本**：只显示正式发布的版本
- **仅快照版本**：只显示快照版本

### 4. 复制版本号

- **双击**版本行：复制版本号
- **右键菜单**：
  - 复制版本号（如 `2.5.6`）
  - 复制完整坐标（如 `com.distinct:distinct-base-data-api:2.5.6`）
  - 在Nexus中打开

## 界面预览

```
┌────────────────────────────────────────────────────┐
│ [刷新所有API模块]                                  │
│ 显示: (•) 全部  ( ) 正式  ( ) 快照                │
├────────────────────────────────────────────────────┤
│ 模块名              │版本号        │类型│上传时间  │
├────────────────────────────────────────────────────┤
│distinct-base-data-api│2.6.1-SNAPSHOT│快照│2026-01-28│
│distinct-base-data-api│2.6.0-SNAPSHOT│快照│2026-01-27│
│distinct-appointment-api│3.2.1-SNAPSHOT│快照│2026-01-28│
└────────────────────────────────────────────────────┘
显示 全部版本: 145 个（共 145 个）
```

## 文档

- [快速开始](docs/QUICK_START.md)
- [开发指南](docs/DEVELOPMENT.md)
- [测试指南](docs/TESTING.md)
- [故障排查](docs/TROUBLESHOOTING.md)
- [更新日志](docs/CHANGELOG.md)
- [升级指南](docs/UPGRADE_GUIDE.md)

## 版本历史

### v1.1.1 (2026-04-07)
- ✅ 移除"仓库"列
- ✅ 修复上传时间显示
- ✅ 首次打开自动加载

### v1.1.0 (2026-04-07)
- ✅ 自动识别所有API模块
- ✅ 版本过滤功能（全部/正式/快照）
- ✅ 批量查询展示
- ✅ 增强错误处理

### v1.0.0 (2026-04-07)
- 初始版本发布

## 开发

### 构建插件

```bash
./gradlew clean buildPlugin
```

### 运行开发环境

```bash
./gradlew runIde
```

这将启动一个安装了插件的IDEA实例。

## 贡献

欢迎贡献！请随时提交Pull Request。

## 许可证

MIT License

## 支持

- 邮箱：gxy2825@gmail.com
- 问题反馈：[GitHub Issues](https://github.com/midasism/nexus-version-manager-plugin/issues)
- JetBrains Marketplace：即将上线...

## 致谢

构建使用：
- Kotlin 1.9.21
- IntelliJ Platform SDK
- OkHttp 4.12.0
- Gson 2.10.1

---

**让Maven版本管理更简单！** 🚀
