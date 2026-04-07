# 提交到JetBrains Marketplace指南

## 📦 准备工作

### 1. 确保插件已构建

```bash
cd /Users/midasgao/code/distinct/nexus-version-manager-plugin
./gradlew clean buildPlugin
```

构建成功后，插件文件位于：
```
build/distributions/nexus-version-manager-plugin-1.1.1.zip
```

文件大小约：**2.9 MB**

### 2. 准备插件信息

以下信息将用于Marketplace提交：

**基本信息：**
- **Plugin Name**: Nexus Version Manager
- **Plugin ID**: com.distinct.nexus-version-manager
- **Version**: 1.1.1
- **Vendor**: Distinct Clinic
- **Vendor Email**: support@distinctclinic.com
- **License**: MIT
- **Category**: Tools Integration

**描述（英文）：**
```
A powerful IntelliJ IDEA plugin for managing Maven versions in private Nexus repositories.

Key Features:
• Auto-detect all Maven modules ending with -api
• Version filtering (All/Release/Snapshot)
• Batch query and display of all API module versions
• Smart version sorting (descending order, releases first)
• Quick copy version numbers (double-click or right-click)
• Nexus integration (open in browser)
• Auto-refresh on first open
• Accurate upload time display

Perfect for teams using private Nexus repositories to manage Maven artifacts.
Compatible with IntelliJ IDEA 2023.1 - 2026.x.
```

**描述（中文）：**
```
强大的IntelliJ IDEA插件，用于管理私有Nexus仓库中的Maven版本。

核心功能：
• 自动识别所有以-api结尾的Maven模块
• 版本过滤（全部/正式/快照）
• 批量查询展示所有API模块版本
• 智能版本排序（倒序，正式版优先）
• 快速复制版本号（双击或右键）
• Nexus集成（浏览器中打开）
• 首次打开自动刷新
• 准确显示上传时间

适合使用私有Nexus仓库管理Maven构件的团队。
兼容IntelliJ IDEA 2023.1 - 2026.x。
```

**标签（Tags）：**
```
maven, nexus, version, management, repository, artifact, api, dependency, tools
```

### 3. 准备截图

需要准备以下截图（建议尺寸 1280x800 或更大）：

1. **主界面截图** - 显示版本列表
2. **配置页面截图** - Settings界面
3. **过滤功能截图** - 展示版本过滤
4. **右键菜单截图** - 展示交互功能

截图建议使用：
- 明亮主题（IntelliJ Light）
- 清晰的示例数据
- 突出核心功能

## 🚀 提交步骤

### 步骤1：注册JetBrains账号

1. 访问 https://plugins.jetbrains.com/
2. 点击右上角 "Sign In"
3. 使用JetBrains Account或GitHub账号登录

### 步骤2：创建新插件

1. 登录后，点击头像 > "Upload plugin"
2. 点击 "Add new plugin"
3. 选择 "Upload plugin file"

### 步骤3：上传插件文件

1. 上传 `nexus-version-manager-plugin-1.1.1.zip`
2. 系统会自动解析plugin.xml中的信息

### 步骤4：填写插件信息

#### Basic Information
```
Name: Nexus Version Manager
Category: Tools Integration
License: MIT
```

#### Description
复制上面准备的英文描述到Description字段

#### Tags
```
maven, nexus, version, management, repository, artifact
```

#### Links
```
GitHub: https://github.com/midasism/nexus-version-manager-plugin
Issues: https://github.com/midasism/nexus-version-manager-plugin/issues
Documentation: https://github.com/midasism/nexus-version-manager-plugin/blob/main/README.md
```

### 步骤5：上传截图

1. 点击 "Screenshots" 标签
2. 上传准备好的截图
3. 添加描述文字
4. 设置主要截图（会在列表页显示）

### 步骤6：兼容性设置

```
Since Build: 231 (2023.1)
Until Build: 263.* (2026.3)
```

确保与plugin.xml中的配置一致。

### 步骤7：提交审核

1. 检查所有信息是否完整
2. 点击 "Submit for review"
3. 等待JetBrains团队审核（通常1-3个工作日）

## 📋 审核清单

在提交前确认：

- [x] 插件已成功构建
- [x] plugin.xml配置正确
- [x] 插件描述清晰完整
- [ ] 已准备3-5张高质量截图
- [x] 兼容性范围设置正确
- [x] 联系方式准确
- [x] LICENSE文件存在
- [x] README文档完整

## 🎨 截图建议

### 截图1：主界面（必需）
**内容：**
- 显示工具窗口
- 表格中有多个模块的版本数据
- 显示版本过滤选项
- 状态栏有统计信息

**标注：**
```
Main interface showing version list with filtering options
```

### 截图2：版本过滤（推荐）
**内容：**
- 切换不同的过滤选项
- 展示过滤效果对比

**标注：**
```
Version filtering: All / Release Only / Snapshot Only
```

### 截图3：配置页面（推荐）
**内容：**
- Settings > Tools > Nexus Version Manager
- 展示配置选项

**标注：**
```
Configuration page for Nexus connection
```

### 截图4：右键菜单（可选）
**内容：**
- 右键菜单展开的效果
- 显示所有菜单项

**标注：**
```
Context menu with copy and open options
```

## 📸 如何截图

### macOS
```
Command + Shift + 4
然后空格键，点击窗口
```

### Windows
```
使用Snipping Tool或Windows + Shift + S
```

### 截图工具推荐
- Snagit
- Lightshot
- IDEA自带的截图工具（View > Tool Windows > Screenshot）

## ⏳ 审核流程

### 审核时间
- 首次提交：1-3个工作日
- 更新版本：1-2个工作日

### 审核标准
JetBrains会检查：
1. **功能性**：插件是否能正常工作
2. **描述准确性**：描述是否与实际功能匹配
3. **代码质量**：基本的代码质量检查
4. **安全性**：是否存在安全问题
5. **兼容性**：是否与声明的版本兼容

### 常见拒绝原因
- 描述不清晰或夸大功能
- 截图质量差或缺失
- 插件有明显Bug
- 兼容性声明不准确
- 缺少必要的文档

## ✅ 审核通过后

### 插件将出现在：
```
https://plugins.jetbrains.com/plugin/XXXXX-nexus-version-manager
```

### 用户可以：
1. 在IDEA中搜索安装：`Settings > Plugins > Marketplace > "Nexus Version Manager"`
2. 自动接收更新通知
3. 在Marketplace页面留评论和评分

### 后续更新：
1. 修改代码，更新版本号
2. 构建新版本
3. 在Marketplace上传新版本zip
4. 等待审核通过

## 📊 发布后的运营

### 1. 推广渠道
- 在公司内部推广使用
- 在技术社区分享（如掘金、CSDN）
- 在GitHub上添加使用说明

### 2. 收集反馈
- 关注GitHub Issues
- 监控JetBrains Marketplace评论
- 收集用户使用反馈

### 3. 持续改进
- 根据反馈优化功能
- 修复发现的Bug
- 添加新功能

## 🎯 目标指标

### 短期目标（1个月）
- [ ] 下载量：50+
- [ ] GitHub Stars: 10+
- [ ] 零Bug报告

### 中期目标（3个月）
- [ ] 下载量：200+
- [ ] GitHub Stars: 30+
- [ ] 添加2-3个新功能

### 长期目标（6个月）
- [ ] 下载量：500+
- [ ] 用户评分：4.5+
- [ ] 成为公司标准开发工具

## 💡 提示

1. **快速通过审核的技巧**
   - 确保截图清晰专业
   - 描述简洁准确
   - 提供完整的使用文档
   - 在plugin.xml中提供详细的change-notes

2. **版本更新最佳实践**
   - 遵循语义化版本号
   - 每次更新提供详细的change notes
   - 保持向后兼容性
   - 在GitHub上同步发布Release

3. **用户支持**
   - 快速响应Issues
   - 提供详细的troubleshooting文档
   - 在README中提供清晰的使用指南

## 📞 需要帮助？

如果在提交过程中遇到问题：

1. **查看官方文档**
   https://plugins.jetbrains.com/docs/marketplace/

2. **联系JetBrains支持**
   marketplace@jetbrains.com

3. **查看社区讨论**
   https://intellij-support.jetbrains.com/

---

**祝你的插件审核顺利通过！** 🎉
