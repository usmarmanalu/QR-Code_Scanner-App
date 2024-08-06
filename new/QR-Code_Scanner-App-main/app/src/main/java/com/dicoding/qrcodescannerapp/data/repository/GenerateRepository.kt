package com.dicoding.qrcodescannerapp.data.repository

import com.dicoding.qrcodescannerapp.data.local.*
import kotlinx.coroutines.flow.*
import javax.inject.*


@Singleton
class GenerateTextRepository @Inject constructor(
    private val generateTextDao: GenerateTextDao
) {

    suspend fun insert(generateTextEntity: GenerateTextEntity) {
        generateTextDao.insert(generateTextEntity)
    }

    fun getAllGenerateHistory(): Flow<List<GenerateTextEntity>> {
        return generateTextDao.getAllGenerateHistory()
    }

    suspend fun deleteGenerateHistory(generateHistory: GenerateTextEntity) {
        return generateTextDao.delete(generateHistory)
    }
}

@Singleton
class GenerateWebRepository @Inject constructor(
    private val generateWebDao: GenerateWebDao
) {

    suspend fun insert(generateWebEntity: GenerateWebEntity) {
        generateWebDao.insert(generateWebEntity)
    }

    fun getAllGenerateHistory(): Flow<List<GenerateWebEntity>> {
        return generateWebDao.getAllGenerateHistory()
    }

    suspend fun deleteGenerateHistory(generateWebEntity: GenerateWebEntity) {
        return generateWebDao.delete(generateWebEntity)
    }
}

@Singleton
class GenerateHistoryWifiRepository @Inject constructor(
    private val generateWifiDao: GenerateWifiDao
) {

    suspend fun insert(generateWifiEntity: GenerateWifiEntity) {
        generateWifiDao.insert(generateWifiEntity)
    }

    fun getAllGenerateWifiHistory(): Flow<List<GenerateWifiEntity>> {
        return generateWifiDao.getAllGenerateWifiHistory()
    }

    suspend fun deleteGenerateWifiHistory(generateWifiEntity: GenerateWifiEntity) {
        return generateWifiDao.delete(generateWifiEntity)
    }
}