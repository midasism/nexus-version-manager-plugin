package com.distinct.nexus.models

data class MavenModule(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val name: String,
    val pomPath: String
) {
    val displayName: String
        get() = "$artifactId ($version)"
    
    val fullCoordinates: String
        get() = "$groupId:$artifactId:$version"
}
