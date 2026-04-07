package com.distinct.nexus.ui

import com.distinct.nexus.services.NexusApiService
import com.distinct.nexus.services.NexusSettingsState
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class NexusSettingsConfigurable : Configurable {
    
    private var settingsPanel: NexusSettingsPanel? = null
    
    override fun getDisplayName(): String {
        return "Nexus Version Manager"
    }
    
    override fun createComponent(): JComponent {
        val panel = NexusSettingsPanel()
        settingsPanel = panel
        
        val settings = NexusSettingsState.getInstance()
        panel.setConfig(settings.toConfig())
        
        return panel.getPanel()
    }
    
    override fun isModified(): Boolean {
        val panel = settingsPanel ?: return false
        val settings = NexusSettingsState.getInstance()
        return panel.isModified(settings.toConfig())
    }
    
    override fun apply() {
        val panel = settingsPanel ?: return
        val config = panel.getConfig()
        
        val settings = NexusSettingsState.getInstance()
        settings.fromConfig(config)
        
        val nexusApiService = ApplicationManager.getApplication().getService(NexusApiService::class.java)
        nexusApiService.updateConfig(config)
    }
    
    override fun reset() {
        val panel = settingsPanel ?: return
        val settings = NexusSettingsState.getInstance()
        panel.setConfig(settings.toConfig())
    }
    
    override fun disposeUIResources() {
        settingsPanel = null
    }
}
