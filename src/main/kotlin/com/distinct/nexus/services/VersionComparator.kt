package com.distinct.nexus.services

import com.distinct.nexus.models.MavenVersion

class VersionComparator : Comparator<MavenVersion> {
    
    override fun compare(v1: MavenVersion, v2: MavenVersion): Int {
        // 正式版本优先于快照版本
        if (v1.isSnapshot != v2.isSnapshot) {
            return if (v1.isSnapshot) 1 else -1
        }
        
        // 同类型版本按版本号比较（倒序）
        return compareVersionStrings(v2.version, v1.version)
    }
    
    companion object {
        fun sortVersions(versions: List<MavenVersion>): List<MavenVersion> {
            return versions.sortedWith(VersionComparator())
        }
        
        fun compareVersionStrings(v1: String, v2: String): Int {
            val parts1 = parseVersion(v1)
            val parts2 = parseVersion(v2)
            
            val maxLength = maxOf(parts1.size, parts2.size)
            
            for (i in 0 until maxLength) {
                val part1 = if (i < parts1.size) parts1[i] else VersionPart(0, "")
                val part2 = if (i < parts2.size) parts2[i] else VersionPart(0, "")
                
                val result = compareParts(part1, part2)
                if (result != 0) return result
            }
            
            return 0
        }
        
        private fun parseVersion(version: String): List<VersionPart> {
            // 移除SNAPSHOT后缀
            val cleanVersion = version.replace("-SNAPSHOT", "", ignoreCase = true)
            
            // 按.和-分隔
            val parts = cleanVersion.split(".", "-")
            
            return parts.map { part ->
                val numericValue = part.toIntOrNull() ?: 0
                VersionPart(numericValue, part)
            }
        }
        
        private fun compareParts(p1: VersionPart, p2: VersionPart): Int {
            // 优先比较数字部分
            if (p1.numeric != p2.numeric) {
                return p1.numeric.compareTo(p2.numeric)
            }
            
            // 数字相同时比较字符串部分
            return p1.text.compareTo(p2.text)
        }
    }
    
    private data class VersionPart(
        val numeric: Int,
        val text: String
    )
}
