# 上传时间字段调试指南

## 问题描述

表格中的"上传时间"列显示为"-"，需要找出Nexus API响应中实际的时间字段名。

## 已做的改进

### 1. 移除"仓库"列 ✅

**修改文件：**
- `VersionTableModel.kt` - 从5列减少到4列
- `NexusVersionToolWindow.kt` - 调整列宽

**新的表格结构：**
```
| 模块名 | 版本号 | 类型 | 上传时间 |
```

### 2. 增强时间戳解析 ✅

**修改文件：** `NexusApiService.kt`

**尝试的字段名：**
- `lastModified`
- `lastUpdated`
- `blobCreated`
- `timestamp`

**添加了调试日志：**
- 打印第一条数据的完整JSON
- 打印每个版本的所有可用字段名

## 如何调试

### 方法1：查看IDEA控制台输出

```bash
cd /Users/midasgao/code/distinct/nexus-version-manager-plugin
./gradlew runIde
```

1. 在测试IDEA中打开Maven项目
2. 点击"刷新所有API模块"
3. 查看运行IDEA的控制台输出：

```
Nexus API响应示例: {"version":"2.6.1-SNAPSHOT","repository":"snapshots",...}
版本 2.6.1-SNAPSHOT 可用字段: version, repository, group, name, ...
```

### 方法2：查看IDEA日志

```bash
# macOS
tail -f ~/Library/Logs/JetBrains/IntelliJIdea*/idea.log

# 过滤Nexus相关日志
tail -f ~/Library/Logs/JetBrains/IntelliJIdea*/idea.log | grep -i "nexus\|版本"
```

### 方法3：手动测试Nexus API

使用curl直接调用Nexus API查看响应：

```bash
curl -X POST 'http://maven.distinctclinic.com:8081/nexus/service/extdirect' \
  -H 'Content-Type: application/json' \
  -H 'NX-ANTI-CSRF-TOKEN: YOUR_TOKEN' \
  -d '{
    "action": "coreui_Search",
    "method": "read",
    "data": [{
      "formatSearch": false,
      "page": 1,
      "start": 0,
      "limit": 10,
      "sort": [{"property": "version", "direction": "DESC"}],
      "filter": [
        {"property": "keyword", "value": "distinct-base-data-api"},
        {"property": "name.raw", "value": "distinct-base-data-api"}
      ]
    }],
    "type": "rpc",
    "tid": 15
  }' | jq '.result.data[0]'
```

查看响应中包含的所有字段。

## 可能的原因

### 1. Nexus版本不同

不同版本的Nexus可能使用不同的字段名：

| Nexus版本 | 时间字段名 |
|-----------|------------|
| Nexus 2.x | `lastModified` |
| Nexus 3.x | `lastModified` 或 `blobCreated` |
| 某些版本 | 可能没有时间字段 |

### 2. 搜索API vs 浏览API

ExtDirect搜索API返回的数据可能比浏览API少。可能需要：
- 使用REST API而非ExtDirect API
- 额外查询每个artifact的详细信息

### 3. 权限问题

某些时间戳字段可能需要特殊权限才能查看。

## 解决方案

### 方案1：根据调试日志修改字段名

运行插件后，根据控制台输出的字段名，修改代码：

```kotlin
// 在NexusApiService.kt的parseResponse方法中
val uploadDate = try {
    when {
        item.has("actualFieldName") -> item.get("actualFieldName")?.asLong
        // 其他尝试...
    }
}
```

### 方案2：使用REST API代替ExtDirect

如果ExtDirect API不返回时间戳，可以改用Nexus REST API：

```kotlin
// 查询详细信息的API
GET /service/rest/v1/search?repository=releases&name=distinct-base-data-api
```

### 方案3：从文件元数据获取

有些情况下需要额外查询artifact的元数据：

```kotlin
GET /repository/releases/com/distinct/distinct-base-data-api/2.6.1/distinct-base-data-api-2.6.1.pom
// 查看HTTP响应头的Last-Modified
```

### 方案4：如果Nexus不提供时间戳

如果Nexus确实不返回时间戳，考虑：
- 隐藏上传时间列
- 或者显示"不可用"

## 快速测试步骤

1. **构建并运行插件：**
   ```bash
   ./gradlew runIde
   ```

2. **查看控制台输出：**
   - 观察"Nexus API响应示例"
   - 观察"可用字段"列表

3. **记录实际字段名：**
   ```
   找到的字段：__________
   ```

4. **修改代码：**
   ```kotlin
   val uploadDate = item.get("实际字段名")?.asLong
   ```

5. **验证：**
   - 重新运行插件
   - 检查上传时间列是否显示

## 临时解决方案

如果短期内无法获取上传时间，可以：

### 选项1：隐藏上传时间列

```kotlin
// VersionTableModel.kt
private val columns = arrayOf("模块名", "版本号", "类型")
```

### 选项2：显示为"不可用"

```kotlin
// VersionTableModel.kt
3 -> if (version.uploadDate != null) {
    dateFormat.format(Date(version.uploadDate))
} else {
    "不可用"
}
```

## 需要的信息

为了帮助你解决这个问题，请提供：

1. **控制台输出：**
   ```
   Nexus API响应示例: {...}
   版本 X 可用字段: ...
   ```

2. **Nexus版本：**
   - 访问 http://maven.distinctclinic.com:8081/nexus
   - 查看页面底部或关于页面的版本信息

3. **手动API测试结果：**
   - 使用上面的curl命令
   - 贴出响应的JSON（至少一条数据）

## 下一步

1. ✅ 运行 `./gradlew runIde`
2. ✅ 查看控制台输出
3. ⏳ 根据输出调整字段名
4. ⏳ 测试验证
5. ⏳ 更新代码

有了调试信息后，我们就能准确知道应该使用哪个字段名了！
