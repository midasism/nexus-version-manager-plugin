package com.distinct.nexus.models

data class NexusConfig(
    var serverUrl: String = "http://maven.distinctclinic.com:8081/nexus",
    var authToken: String = "",
    var timeout: Int = 30
) {
    val serviceUrl: String
        get() = "$serverUrl/service/extdirect"
}
