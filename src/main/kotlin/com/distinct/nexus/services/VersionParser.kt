package com.distinct.nexus.services

import com.distinct.nexus.models.MavenVersion

object VersionParser {
    
    fun isSnapshot(version: String): Boolean {
        return version.contains("SNAPSHOT", ignoreCase = true)
    }
    
    fun getNextVersion(currentVersion: String): String {
        // 移除SNAPSHOT后缀
        val cleanVersion = currentVersion.replace("-SNAPSHOT", "", ignoreCase = true)
        
        // 解析版本号
        val parts = cleanVersion.split(".")
        if (parts.isEmpty()) {
            return "1.0.0"
        }
        
        val newParts = parts.toMutableList()
        
        // 递增最后一个数字部分
        val lastIndex = newParts.lastIndex
        val lastPart = newParts[lastIndex].toIntOrNull()
        
        if (lastPart != null) {
            newParts[lastIndex] = (lastPart + 1).toString()
        }
        
        return newParts.joinToString(".")
    }
    
    fun snapshotToRelease(version: String): String {
        return version.replace("-SNAPSHOT", "", ignoreCase = true)
    }
    
    fun releaseToSnapshot(version: String): String {
        if (isSnapshot(version)) {
            return version
        }
        return "$version-SNAPSHOT"
    }
    
    fun groupByType(versions: List<MavenVersion>): Map<String, List<MavenVersion>> {
        return versions.groupBy { if (it.isSnapshot) "快照版本" else "正式版本" }
    }
    
    fun filterByRepository(versions: List<MavenVersion>, repository: String): List<MavenVersion> {
        return versions.filter { it.repository.contains(repository, ignoreCase = true) }
    }
    
    fun getLatestRelease(versions: List<MavenVersion>): MavenVersion? {
        return versions
            .filter { !it.isSnapshot }
            .sortedWith(VersionComparator())
            .firstOrNull()
    }
    
    fun getLatestSnapshot(versions: List<MavenVersion>): MavenVersion? {
        return versions
            .filter { it.isSnapshot }
            .sortedWith(VersionComparator())
            .firstOrNull()
    }
}
