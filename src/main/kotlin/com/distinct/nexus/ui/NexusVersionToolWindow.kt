package com.distinct.nexus.ui

import com.distinct.nexus.models.MavenModule
import com.distinct.nexus.models.MavenVersion
import com.distinct.nexus.services.NexusApiService
import com.distinct.nexus.services.ProjectInfoService
import com.distinct.nexus.services.VersionComparator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class NexusVersionToolWindow(private val project: Project) {
    
    private val tableModel = VersionTableModel()
    private val table = JBTable(tableModel)
    private val refreshButton = JButton("刷新所有API模块")
    private val statusLabel = JLabel("点击刷新按钮加载API模块版本")
    
    // 版本过滤选项
    private val allVersionsRadio = JRadioButton("全部版本", true)
    private val releaseOnlyRadio = JRadioButton("仅正式版本")
    private val snapshotOnlyRadio = JRadioButton("仅快照版本")
    private val versionFilterGroup = ButtonGroup()
    
    private val nexusApiService: NexusApiService
    private val projectInfoService: ProjectInfoService
    
    private var allVersions = listOf<MavenVersion>()
    
    init {
        // 延迟获取service，确保在插件完全加载后
        nexusApiService = ApplicationManager.getApplication().getService(NexusApiService::class.java)
        projectInfoService = project.getService(ProjectInfoService::class.java)
        
        // 设置过滤按钮组
        versionFilterGroup.add(allVersionsRadio)
        versionFilterGroup.add(releaseOnlyRadio)
        versionFilterGroup.add(snapshotOnlyRadio)
        
        // 添加过滤监听器
        allVersionsRadio.addActionListener { applyVersionFilter() }
        releaseOnlyRadio.addActionListener { applyVersionFilter() }
        snapshotOnlyRadio.addActionListener { applyVersionFilter() }
    }
    
    fun getContent(): JComponent {
        val panel = JPanel(BorderLayout())
        
        // 顶部工具栏
        val toolbar = createToolbar()
        panel.add(toolbar, BorderLayout.NORTH)
        
        // 中间表格
        setupTable()
        val scrollPane = JBScrollPane(table)
        panel.add(scrollPane, BorderLayout.CENTER)
        
        // 底部状态栏
        panel.add(statusLabel, BorderLayout.SOUTH)
        
        // 自动加载数据（延迟执行，确保UI已完全初始化）
        SwingUtilities.invokeLater {
            // 延迟1秒后自动刷新，确保Maven项目已加载
            Thread {
                Thread.sleep(1000)
                ApplicationManager.getApplication().invokeLater {
                    refreshAllApiModules()
                }
            }.start()
        }
        
        return panel
    }
    
    private fun createToolbar(): JPanel {
        val mainToolbar = JPanel(BorderLayout())
        
        // 上层：刷新按钮
        val topPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        refreshButton.addActionListener { refreshAllApiModules() }
        topPanel.add(refreshButton)
        mainToolbar.add(topPanel, BorderLayout.NORTH)
        
        // 下层：版本过滤选项
        val filterPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        filterPanel.add(JLabel("显示: "))
        filterPanel.add(allVersionsRadio)
        filterPanel.add(releaseOnlyRadio)
        filterPanel.add(snapshotOnlyRadio)
        mainToolbar.add(filterPanel, BorderLayout.CENTER)
        
        return mainToolbar
    }
    
    private fun setupTable() {
        table.setShowGrid(true)
        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        
        // 设置列宽: 模块名、版本号、类型、上传时间
        table.columnModel.getColumn(0).preferredWidth = 200  // 模块名
        table.columnModel.getColumn(1).preferredWidth = 150  // 版本号
        table.columnModel.getColumn(2).preferredWidth = 80   // 类型
        table.columnModel.getColumn(3).preferredWidth = 150  // 上传时间
        
        // 双击复制版本号
        table.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    val row = table.selectedRow
                    if (row >= 0) {
                        val version = tableModel.getVersion(row)
                        version?.let { copyToClipboard(it.version) }
                    }
                }
            }
        })
        
        // 右键菜单
        val popupMenu = createPopupMenu()
        table.componentPopupMenu = popupMenu
    }
    
    private fun createPopupMenu(): JPopupMenu {
        val menu = JPopupMenu()
        
        val copyVersionItem = JMenuItem("复制版本号")
        copyVersionItem.addActionListener {
            val row = table.selectedRow
            if (row >= 0) {
                val version = tableModel.getVersion(row)
                version?.let { copyToClipboard(it.version) }
            }
        }
        menu.add(copyVersionItem)
        
        val copyCoordinatesItem = JMenuItem("复制完整坐标")
        copyCoordinatesItem.addActionListener {
            val row = table.selectedRow
            if (row >= 0) {
                val version = tableModel.getVersion(row)
                version?.let {
                    val coordinates = "${it.groupId}:${it.artifactId}:${it.version}"
                    copyToClipboard(coordinates)
                }
            }
        }
        menu.add(copyCoordinatesItem)
        
        menu.addSeparator()
        
        val openInBrowserItem = JMenuItem("在Nexus中打开")
        openInBrowserItem.addActionListener {
            val row = table.selectedRow
            if (row >= 0) {
                val version = tableModel.getVersion(row)
                version?.let { openInBrowser(it) }
            }
        }
        menu.add(openInBrowserItem)
        
        return menu
    }
    
    private fun refreshAllApiModules() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "查询API模块版本", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = false
                
                ApplicationManager.getApplication().invokeLater {
                    statusLabel.text = "正在获取API模块列表..."
                }
                
                // 获取所有-api结尾的模块
                val apiModules = try {
                    projectInfoService.getApiModules()
                } catch (e: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        Messages.showErrorDialog(
                            project,
                            "获取模块列表失败: ${e.message}\n\n请确保Maven项目已完全加载。",
                            "错误"
                        )
                        statusLabel.text = "获取模块失败"
                    }
                    return
                }
                
                if (apiModules.isEmpty()) {
                    ApplicationManager.getApplication().invokeLater {
                        Messages.showInfoMessage(
                            project,
                            "未找到以-api结尾的Maven模块。\n\n请确保：\n1. 项目是Maven项目\n2. 包含以-api结尾的模块",
                            "提示"
                        )
                        statusLabel.text = "未找到API模块"
                    }
                    return
                }
                
                ApplicationManager.getApplication().invokeLater {
                    statusLabel.text = "找到 ${apiModules.size} 个API模块，正在查询版本..."
                }
                
                val allVersionsList = mutableListOf<MavenVersion>()
                
                // 查询所有API模块的版本
                apiModules.forEachIndexed { index, module ->
                    indicator.fraction = (index + 1).toDouble() / apiModules.size
                    indicator.text = "正在查询 ${module.artifactId} (${index + 1}/${apiModules.size})"
                    
                    val result = nexusApiService.fetchVersions(
                        module.groupId,
                        module.artifactId
                    )
                    
                    result.fold(
                        onSuccess = { versions ->
                            allVersionsList.addAll(versions)
                        },
                        onFailure = { error ->
                            // 记录失败但继续
                            println("查询 ${module.artifactId} 失败: ${error.message}")
                        }
                    )
                }
                
                ApplicationManager.getApplication().invokeLater {
                    if (allVersionsList.isEmpty()) {
                        Messages.showWarningDialog(
                            project,
                            "未查询到任何版本信息。\n\n请检查：\n1. Nexus配置是否正确\n2. 网络连接是否正常\n3. 认证Token是否有效",
                            "提示"
                        )
                        statusLabel.text = "未查询到版本"
                    } else {
                        allVersions = VersionComparator.sortVersions(allVersionsList)
                        applyVersionFilter()
                        statusLabel.text = "共找到 ${allVersionsList.size} 个版本，来自 ${apiModules.size} 个API模块"
                    }
                }
            }
        })
    }
    
    private fun applyVersionFilter() {
        val filteredVersions = when {
            releaseOnlyRadio.isSelected -> allVersions.filter { !it.isSnapshot }
            snapshotOnlyRadio.isSelected -> allVersions.filter { it.isSnapshot }
            else -> allVersions
        }
        
        tableModel.setVersions(filteredVersions)
        
        val filterType = when {
            releaseOnlyRadio.isSelected -> "正式版本"
            snapshotOnlyRadio.isSelected -> "快照版本"
            else -> "全部版本"
        }
        
        if (allVersions.isNotEmpty()) {
            statusLabel.text = "显示 $filterType: ${filteredVersions.size} 个（共 ${allVersions.size} 个）"
        }
    }
    
    private fun copyToClipboard(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), null)
        statusLabel.text = "已复制到剪贴板: $text"
    }
    
    private fun openInBrowser(version: com.distinct.nexus.models.MavenVersion) {
        try {
            val config = nexusApiService.getConfig()
            val url = "${config.serverUrl}/#browse/search=keyword%3D${version.artifactId}"
            java.awt.Desktop.getDesktop().browse(java.net.URI(url))
        } catch (e: Exception) {
            Messages.showErrorDialog(project, "打开浏览器失败: ${e.message}", "错误")
        }
    }
}
