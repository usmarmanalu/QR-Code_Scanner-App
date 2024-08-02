package com.dicoding.qrcodescannerapp.data.local

import androidx.room.*


//scanned
@Entity(tableName = "scan_history")
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val result: String,
    val timestamp: Long
)