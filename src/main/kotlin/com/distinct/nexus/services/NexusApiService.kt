package com.distinct.nexus.services

import com.distinct.nexus.models.MavenVersion
import com.distinct.nexus.models.NexusConfig
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.intellij.openapi.components.Service
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

@Service
class NexusApiService {
    private val gson = Gson()
    private var config: NexusConfig
    
    init {
        // 在初始化时加载已保存的配置
        config = try {
            NexusSettingsState.getInstance().toConfig()
        } catch (e: Exception) {
            println("加载Nexus配置失败: ${e.message}")
            NexusConfig()
        }
    }
    
    fun updateConfig(newConfig: NexusConfig) {
        this.config = newConfig
    }
    
    fun getConfig(): NexusConfig {
        return config
    }
    
    fun fetchVersions(groupId: String, artifactId: String): Result<List<MavenVersion>> {
        return try {
            val client = OkHttpClient.Builder()
                .connectTimeout(config.timeout.toLong(), TimeUnit.SECONDS)
                .readTimeout(config.timeout.toLong(), TimeUnit.SECONDS)
                .build()
            
            val requestBody = buildRequestBody(artifactId)
            val jsonBody = gson.toJson(requestBody)
            
            val request = Request.Builder()
                .url(config.serviceUrl)
                .post(jsonBody.toRequestBody("application/json".toMediaType()))
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .header("X-Nexus-UI", "true")
                .header("X-Requested-With", "XMLHttpRequest")
                .apply {
                    if (config.authToken.isNotEmpty()) {
                        header("NX-ANTI-CSRF-TOKEN", config.authToken)
                        header("Cookie", "NX-ANTI-CSRF-TOKEN=${config.authToken}")
                    }
                }
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                return Result.failure(Exception("HTTP错误: ${response.code}"))
            }
            
            val responseBody = response.body?.string() ?: return Result.failure(Exception("响应为空"))
            val versions = parseResponse(responseBody, groupId, artifactId)
            
            Result.success(versions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun buildRequestBody(artifactId: String): Map<String, Any> {
        return mapOf(
            "action" to "coreui_Search",
            "method" to "read",
            "data" to listOf(
                mapOf(
                    "formatSearch" to false,
                    "page" to 1,
                    "start" to 0,
                    "limit" to 300,
                    "sort" to listOf(
                        mapOf(
                            "property" to "version",
                            "direction" to "DESC"
                        )
                    ),
                    "filter" to listOf(
                        mapOf(
                            "property" to "keyword",
                            "value" to artifactId
                        ),
                        mapOf(
                            "property" to "name.raw",
                            "value" to artifactId
                        )
                    )
                )
            ),
            "type" to "rpc",
            "tid" to 15
        )
    }
    
    private fun parseResponse(responseBody: String, groupId: String, artifactId: String): List<MavenVersion> {
        val versions = mutableListOf<MavenVersion>()
        
        try {
            val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
            val result = jsonResponse.getAsJsonObject("result")
            val data = result?.getAsJsonArray("data")
            
            data?.forEach { element ->
                val item = element.asJsonObject
                val version = item.get("version")?.asString ?: return@forEach
                val repository = item.get("repositoryName")?.asString ?: ""
                
                // 解析lastBlobUpdated字段（ISO 8601格式）
                val uploadDate = try {
                    if (item.has("lastBlobUpdated")) {
                        val dateStr = item.get("lastBlobUpdated")?.asString
                        if (dateStr != null) {
                            // 解析ISO 8601格式: "2026-01-28T05:04:20.555Z"
                            parseIso8601ToTimestamp(dateStr)
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    println("解析时间戳失败: ${e.message}")
                    null
                }
                
                versions.add(
                    MavenVersion.from(
                        version = version,
                        groupId = groupId,
                        artifactId = artifactId,
                        repository = repository,
                        uploadDate = uploadDate
                    )
                )
            }
        } catch (e: Exception) {
            throw Exception("解析响应失败: ${e.message}", e)
        }
        
        return versions
    }
    
    private fun parseIso8601ToTimestamp(dateStr: String): Long {
        return try {
            // ISO 8601格式: "2026-01-28T05:04:20.555Z"
            val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            format.timeZone = java.util.TimeZone.getTimeZone("UTC")
            format.parse(dateStr)?.time ?: 0L
        } catch (e: Exception) {
            // 尝试不带毫秒的格式
            try {
                val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                format.timeZone = java.util.TimeZone.getTimeZone("UTC")
                format.parse(dateStr)?.time ?: 0L
            } catch (e2: Exception) {
                println("无法解析日期格式: $dateStr")
                0L
            }
        }
    }
    
    fun testConnection(): Result<String> {
        return try {
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build()
            
            val request = Request.Builder()
                .url(config.serverUrl)
                .get()
                .build()
            
            val response = client.newCall(request).execute()
            
            if (response.isSuccessful) {
                Result.success("连接成功")
            } else {
                Result.failure(Exception("连接失败: HTTP ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("连接失败: ${e.message}"))
        }
    }
}
