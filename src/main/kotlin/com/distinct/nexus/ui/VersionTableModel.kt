package com.distinct.nexus.ui

import com.distinct.nexus.models.MavenVersion
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.table.AbstractTableModel

class VersionTableModel : AbstractTableModel() {
    
    private val columns = arrayOf("模块名", "版本号", "类型", "上传时间")
    private val versions = mutableListOf<MavenVersion>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    
    override fun getRowCount(): Int = versions.size
    
    override fun getColumnCount(): Int = columns.size
    
    override fun getColumnName(column: Int): String = columns[column]
    
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val version = versions[rowIndex]
        return when (columnIndex) {
            0 -> version.artifactId
            1 -> version.version
            2 -> version.displayType
            3 -> version.uploadDate?.let { 
                dateFormat.format(Date(it)) 
            } ?: "-"
            else -> ""
        }
    }
    
    fun setVersions(newVersions: List<MavenVersion>) {
        versions.clear()
        versions.addAll(newVersions)
        fireTableDataChanged()
    }
    
    fun getVersion(row: Int): MavenVersion? {
        return if (row in 0 until versions.size) versions[row] else null
    }
    
    fun clear() {
        versions.clear()
        fireTableDataChanged()
    }
}
