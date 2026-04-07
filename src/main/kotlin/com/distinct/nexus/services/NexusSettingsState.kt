package com.distinct.nexus.services

import com.distinct.nexus.models.NexusConfig
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "NexusVersionManagerSettings",
    storages = [Storage("nexus-version-manager.xml")]
)
class NexusSettingsState : PersistentStateComponent<NexusSettingsState> {
    
    var serverUrl: String = "http://maven.distinctclinic.com:8081/nexus"
    var authToken: String = ""
    var timeout: Int = 30
    
    override fun getState(): NexusSettingsState {
        return this
    }
    
    override fun loadState(state: NexusSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
    
    fun toConfig(): NexusConfig {
        return NexusConfig(
            serverUrl = serverUrl,
            authToken = authToken,
            timeout = timeout
        )
    }
    
    fun fromConfig(config: NexusConfig) {
        serverUrl = config.serverUrl
        authToken = config.authToken
        timeout = config.timeout
    }
    
    companion object {
        fun getInstance(): NexusSettingsState {
            return service()
        }
    }
}
