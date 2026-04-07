package com.distinct.nexus.ui

import com.distinct.nexus.models.NexusConfig
import com.distinct.nexus.services.NexusApiService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JPasswordField

class NexusSettingsPanel {
    
    private val serverUrlField = JBTextField()
    private val authTokenField = JPasswordField()
    private val timeoutField = JBTextField()
    private val testButton = JButton("测试连接")
    
    private val panel = JPanel(GridBagLayout())
    
    init {
        setupUI()
    }
    
    private fun setupUI() {
        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.insets = Insets(5, 5, 5, 5)
        
        // Server URL
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weightx = 0.0
        panel.add(JBLabel("Nexus服务器地址:"), gbc)
        
        gbc.gridx = 1
        gbc.weightx = 1.0
        serverUrlField.text = "http://maven.distinctclinic.com:8081/nexus"
        panel.add(serverUrlField, gbc)
        
        // Auth Token
        gbc.gridx = 0
        gbc.gridy = 1
        gbc.weightx = 0.0
        panel.add(JBLabel("认证Token:"), gbc)
        
        gbc.gridx = 1
        gbc.weightx = 1.0
        panel.add(authTokenField, gbc)
        
        // Help text
        gbc.gridx = 1
        gbc.gridy = 2
        val helpLabel = JBLabel("<html><i>从浏览器Cookie中获取NX-ANTI-CSRF-TOKEN的值</i></html>")
        panel.add(helpLabel, gbc)
        
        // Timeout
        gbc.gridx = 0
        gbc.gridy = 3
        gbc.weightx = 0.0
        panel.add(JBLabel("连接超时(秒):"), gbc)
        
        gbc.gridx = 1
        gbc.weightx = 1.0
        timeoutField.text = "30"
        panel.add(timeoutField, gbc)
        
        // Test button
        gbc.gridx = 1
        gbc.gridy = 4
        gbc.weightx = 0.0
        gbc.anchor = GridBagConstraints.WEST
        testButton.addActionListener { testConnection() }
        panel.add(testButton, gbc)
        
        // Filler
        gbc.gridx = 0
        gbc.gridy = 5
        gbc.gridwidth = 2
        gbc.weighty = 1.0
        gbc.fill = GridBagConstraints.BOTH
        panel.add(JPanel(), gbc)
    }
    
    fun getPanel(): JPanel = panel
    
    fun getConfig(): NexusConfig {
        return NexusConfig(
            serverUrl = serverUrlField.text.trim(),
            authToken = String(authTokenField.password).trim(),
            timeout = timeoutField.text.toIntOrNull() ?: 30
        )
    }
    
    fun setConfig(config: NexusConfig) {
        serverUrlField.text = config.serverUrl
        authTokenField.text = config.authToken
        timeoutField.text = config.timeout.toString()
    }
    
    fun isModified(config: NexusConfig): Boolean {
        return serverUrlField.text.trim() != config.serverUrl ||
                String(authTokenField.password).trim() != config.authToken ||
                (timeoutField.text.toIntOrNull() ?: 30) != config.timeout
    }
    
    private fun testConnection() {
        testButton.isEnabled = false
        testButton.text = "测试中..."
        
        Thread {
            try {
                val nexusApiService = ApplicationManager.getApplication().getService(NexusApiService::class.java)
                val tempConfig = getConfig()
                nexusApiService.updateConfig(tempConfig)
                
                val result = nexusApiService.testConnection()
                
                ApplicationManager.getApplication().invokeLater {
                    result.fold(
                        onSuccess = { message ->
                            Messages.showInfoMessage(message, "连接测试")
                        },
                        onFailure = { error ->
                            Messages.showErrorDialog(error.message, "连接测试失败")
                        }
                    )
                    testButton.isEnabled = true
                    testButton.text = "测试连接"
                }
            } catch (e: Exception) {
                ApplicationManager.getApplication().invokeLater {
                    Messages.showErrorDialog("测试失败: ${e.message}", "错误")
                    testButton.isEnabled = true
                    testButton.text = "测试连接"
                }
            }
        }.start()
    }
}
