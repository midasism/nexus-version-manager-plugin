package com.distinct.nexus.services

import com.distinct.nexus.models.MavenModule
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.idea.maven.project.MavenProjectsManager
import javax.xml.parsers.DocumentBuilderFactory

@Service(Service.Level.PROJECT)
class ProjectInfoService(private val project: Project) {
    
    fun getAllMavenModules(): List<MavenModule> {
        val modules = mutableListOf<MavenModule>()
        
        try {
            val mavenProjectsManager = MavenProjectsManager.getInstance(project)
            
            // 检查Maven项目是否已初始化
            if (!mavenProjectsManager.isMavenizedProject) {
                println("项目不是Maven项目或Maven尚未初始化")
                return emptyList()
            }
            
            val mavenProjects = mavenProjectsManager.projects
            
            if (mavenProjects.isEmpty()) {
                println("Maven项目列表为空，可能尚未完全加载")
                return emptyList()
            }
            
            for (mavenProject in mavenProjects) {
                try {
                    val mavenId = mavenProject.mavenId
                    val groupId = mavenId.groupId
                    val artifactId = mavenId.artifactId
                    val version = mavenId.version
                    
                    // 确保关键字段不为空
                    if (groupId.isNullOrEmpty() || artifactId.isNullOrEmpty()) {
                        continue
                    }
                    
                    val name = mavenProject.name ?: artifactId
                    val pomPath = mavenProject.file?.path ?: ""
                    
                    modules.add(
                        MavenModule(
                            groupId = groupId,
                            artifactId = artifactId,
                            version = version ?: "unknown",
                            name = name,
                            pomPath = pomPath
                        )
                    )
                } catch (e: Exception) {
                    println("解析Maven模块失败: ${e.message}")
                    continue
                }
            }
        } catch (e: Exception) {
            println("获取Maven模块列表失败: ${e.message}")
            e.printStackTrace()
        }
        
        return modules
    }
    
    fun getCurrentModule(): MavenModule? {
        val modules = getAllMavenModules()
        return modules.firstOrNull()
    }
    
    fun findModuleByArtifactId(artifactId: String): MavenModule? {
        return getAllMavenModules().find { it.artifactId == artifactId }
    }
    
    fun parsePomFile(pomFile: VirtualFile): MavenModule? {
        return try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(pomFile.inputStream)
            doc.documentElement.normalize()
            
            val groupId = getElementValue(doc, "groupId") ?: return null
            val artifactId = getElementValue(doc, "artifactId") ?: return null
            val version = getElementValue(doc, "version") ?: "unknown"
            val name = getElementValue(doc, "name") ?: artifactId
            
            MavenModule(
                groupId = groupId,
                artifactId = artifactId,
                version = version,
                name = name,
                pomPath = pomFile.path
            )
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getElementValue(doc: org.w3c.dom.Document, tagName: String): String? {
        val elements = doc.getElementsByTagName(tagName)
        if (elements.length > 0) {
            val element = elements.item(0)
            return element.textContent?.trim()
        }
        return null
    }
    
    fun isApiModule(module: MavenModule): Boolean {
        return module.artifactId.endsWith("-api")
    }
    
    fun getApiModules(): List<MavenModule> {
        val allModules = getAllMavenModules()
        val apiModules = allModules.filter { isApiModule(it) }
        
        println("总模块数: ${allModules.size}, API模块数: ${apiModules.size}")
        apiModules.forEach { 
            println("  - ${it.artifactId} (${it.groupId})")
        }
        
        return apiModules
    }
    
    fun isMavenProject(): Boolean {
        return try {
            val mavenProjectsManager = MavenProjectsManager.getInstance(project)
            mavenProjectsManager.isMavenizedProject
        } catch (e: Exception) {
            false
        }
    }
}
