package com.distinct.nexus.models

data class MavenVersion(
    val version: String,
    val isSnapshot: Boolean,
    val repository: String,
    val uploadDate: Long?,
    val groupId: String,
    val artifactId: String
) {
    val displayType: String
        get() = if (isSnapshot) "快照" else "正式"
    
    companion object {
        fun from(
            version: String,
            groupId: String,
            artifactId: String,
            repository: String = "",
            uploadDate: Long? = null
        ): MavenVersion {
            return MavenVersion(
                version = version,
                isSnapshot = version.contains("SNAPSHOT", ignoreCase = true),
                repository = repository,
                uploadDate = uploadDate,
                groupId = groupId,
                artifactId = artifactId
            )
        }
    }
}
