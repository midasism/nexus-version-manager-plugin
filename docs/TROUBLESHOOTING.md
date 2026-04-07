# 常见问题排查指南

## 问题1：构建的插件安装后看不到模块

### 症状
- 使用`./gradlew runIde`时能看到模块和数据
- 使用`./gradlew buildPlugin`构建后安装，工具窗口空白或无法加载模块

### 可能原因
1. Maven项目未完全加载
2. 插件Service未正确初始化
3. 配置未正确保存或加载

### 解决方案

#### 方案1：等待Maven项目加载完成
```
1. 打开IDEA项目
2. 等待底部状态栏显示"Maven项目导入完成"
3. 检查右侧Maven面板是否显示所有模块
4. 再打开Nexus Versions工具窗口
5. 点击"刷新所有API模块"按钮
```

#### 方案2：检查项目是否是Maven项目
```
1. 查看项目根目录是否有pom.xml
2. 检查IDEA是否识别为Maven项目
3. 右键项目 > Add Framework Support > Maven
```

#### 方案3：查看IDEA日志
```
1. Help > Show Log in Finder (macOS) 或 Show Log in Explorer (Windows)
2. 打开idea.log文件
3. 搜索"ProjectInfoService"或"NexusVersionToolWindow"
4. 查看是否有错误日志
```

#### 方案4：重新导入Maven项目
```
1. 右键项目根目录
2. Maven > Reload Project
3. 等待导入完成
4. 重新打开工具窗口
```

### 调试检查清单
- [ ] Maven项目是否完全加载？
- [ ] 项目是否包含`-api`结尾的模块？
- [ ] 插件配置是否正确（Settings > Tools > Nexus Version Manager）？
- [ ] Nexus服务器是否可访问？
- [ ] 认证Token是否有效？

---

## 问题2：点击刷新后提示"未找到API模块"

### 症状
点击"刷新所有API模块"按钮后，弹窗提示"未找到以-api结尾的Maven模块"

### 可能原因
1. 项目中确实没有`-api`结尾的模块
2. Maven项目结构识别失败
3. 模块命名不符合规范

### 解决方案

#### 方案1：检查模块命名
```
确认项目中是否有以下格式的模块：
✅ distinct-base-data-api
✅ distinct-appointment-api
❌ distinct-base-data-service (不是API模块)
❌ base-data (缺少-api后缀)
```

#### 方案2：查看日志输出
```
1. 打开IDEA控制台
2. 点击刷新按钮
3. 查看控制台输出的调试信息：
   "总模块数: X, API模块数: Y"
   "  - 模块名1 (groupId)"
   "  - 模块名2 (groupId)"
```

#### 方案3：手动验证Maven模块
```
1. 打开右侧Maven面板
2. 展开项目结构
3. 检查是否显示所有模块
4. 如果Maven面板也显示不全，执行Reload Project
```

---

## 问题3：查询版本返回空列表

### 症状
刷新成功，但表格中没有任何版本信息

### 可能原因
1. Nexus服务器配置错误
2. 认证Token失效或错误
3. 模块在Nexus中不存在
4. 网络连接问题

### 解决方案

#### 方案1：测试Nexus连接
```
1. Settings > Tools > Nexus Version Manager
2. 点击"测试连接"按钮
3. 如果失败，检查服务器地址是否正确
```

#### 方案2：验证认证Token
```
1. 浏览器访问Nexus并登录
2. F12打开开发者工具
3. Application > Cookies > 查找NX-ANTI-CSRF-TOKEN
4. 复制最新的Token值到插件配置
5. 重新测试连接
```

#### 方案3：手动验证模块存在性
```
1. 浏览器访问Nexus
2. 搜索对应的artifactId
3. 确认该模块是否已发布到仓库
```

#### 方案4：检查网络
```
1. 确认IDEA可以访问外网
2. 检查公司防火墙/代理设置
3. 在终端测试：curl http://maven.distinctclinic.com:8081/nexus
```

---

## 问题4：版本过滤不生效

### 症状
点击"仅正式版本"或"仅快照版本"，表格没有变化

### 可能原因
1. 所有版本都是同一类型
2. 版本数据未正确加载
3. UI事件监听器未触发

### 解决方案

#### 方案1：检查版本类型
```
查看"类型"列：
- 如果全是"正式"，则点击"仅快照版本"会显示空
- 如果全是"快照"，则点击"仅正式版本"会显示空
```

#### 方案2：重新加载数据
```
1. 点击"全部版本"单选按钮
2. 再次点击"刷新所有API模块"
3. 等待加载完成
4. 再尝试切换过滤选项
```

---

## 问题5：双击复制版本号不工作

### 症状
双击表格行，版本号没有复制到剪贴板

### 解决方案

#### 方案1：使用右键菜单
```
1. 右键点击版本行
2. 选择"复制版本号"或"复制完整坐标"
```

#### 方案2：检查剪贴板权限
```
macOS:
1. System Preferences > Security & Privacy > Privacy
2. Accessibility > 确保IDEA有权限

Windows:
1. 检查杀毒软件是否阻止了剪贴板访问
```

---

## 问题6：插件加载慢或卡顿

### 症状
打开工具窗口很慢，或查询时IDEA卡住

### 可能原因
1. Maven项目太大，模块太多
2. Nexus服务器响应慢
3. 网络延迟高

### 解决方案

#### 方案1：减少查询模块数量
```
目前插件会查询所有-api模块，如果模块太多：
1. 考虑只查询关心的模块（需要代码修改）
2. 或者在Settings中增加模块过滤配置（后续版本）
```

#### 方案2：增加超时时间
```
1. Settings > Tools > Nexus Version Manager
2. 将连接超时从30秒增加到60秒
3. Apply并保存
```

#### 方案3：检查Nexus性能
```
1. 浏览器访问Nexus查询页面
2. 如果Nexus本身也很慢，联系管理员优化
```

---

## 问题7：插件更新后配置丢失

### 症状
插件更新到新版本后，Nexus配置需要重新填写

### 解决方案

#### 方案1：备份配置文件
```
配置文件位置：
macOS: ~/Library/Application Support/JetBrains/<IDEA版本>/options/nexus-version-manager.xml
Windows: %APPDATA%\JetBrains\<IDEA版本>\options\nexus-version-manager.xml

更新前备份此文件，更新后恢复即可
```

#### 方案2：记录配置
```
在更新前，将以下信息记录下来：
- Nexus服务器地址
- 认证Token
- 超时时间
```

---

## 调试技巧

### 1. 查看详细日志
在代码中添加的`println`语句会输出到IDEA控制台：
```
1. View > Tool Windows > Run (如果是runIde)
2. 或者查看idea.log日志文件
```

### 2. 重新加载插件
不需要重启IDEA：
```
1. Ctrl+Alt+Shift+/ (或Cmd+Shift+A)
2. 输入"Registry"
3. 找到"ide.plugins.snapshot.on.unload.fail"
4. 勾选它
5. 在Plugins页面禁用再启用插件
```

### 3. 清除插件缓存
```
1. 关闭IDEA
2. 删除插件缓存目录
3. 重新启动IDEA
```

---

## 获取帮助

如果以上方法都无法解决问题：

1. **查看日志**：Help > Show Log in Finder/Explorer
2. **截图错误**：截取错误提示和相关配置
3. **联系支持**：support@distinctclinic.com
4. **提供信息**：
   - IDEA版本
   - 插件版本
   - 操作系统
   - Maven项目结构
   - 错误日志

---

## 版本兼容性检查

| IDEA版本 | 插件版本 | 状态 |
|----------|----------|------|
| 2023.1+ | 1.1.0 | ✅ 支持 |
| 2022.x | 1.1.0 | ⚠️ 未测试 |
| 2021.x | 1.1.0 | ❌ 不支持 |

如果你的IDEA版本较老，考虑升级到2023.1或更高版本。
