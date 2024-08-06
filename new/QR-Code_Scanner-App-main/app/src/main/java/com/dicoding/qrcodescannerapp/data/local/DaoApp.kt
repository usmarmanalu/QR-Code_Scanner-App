package com.dicoding.qrcodescannerapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.*

//Scanned
@Dao
interface ScanHistoryDao {
    @Insert
    suspend fun insert(scanHistory: ScanHistoryEntity)

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    fun getAllScanHistory(): Flow<List<ScanHistoryEntity>>

    @Delete
     suspend fun delete(scanHistory: ScanHistoryEntity)
}

//Generate
@Dao
interface GenerateTextDao {
    @Insert
    suspend fun insert(generateHistory: GenerateTextEntity)

    @Query("SELECT * FROM generate_text ORDER BY timestamp DESC")
    fun getAllGenerateHistory(): Flow<List<GenerateTextEntity>>

    @Delete
    suspend fun delete(generateHistory: GenerateTextEntity)

}

@Dao
interface GenerateWebDao {
    @Insert
    suspend fun insert(generateWebEntity: GenerateWebEntity)

    @Query("SELECT * FROM generate_web ORDER BY timestamp DESC")
    fun getAllGenerateHistory(): Flow<List<GenerateWebEntity>>

    @Delete
    suspend fun delete(generateHistory: GenerateWebEntity)

}

@Dao
interface GenerateWifiDao {
    @Insert
    suspend fun insert(generateWifiEntity: GenerateWifiEntity)

    @Query("SELECT * FROM generate_wifi ORDER BY timestamp DESC")
    fun getAllGenerateWifiHistory(): Flow<List<GenerateWifiEntity>>

    @Delete
    suspend fun delete(generateWifiEntity: GenerateWifiEntity)

}