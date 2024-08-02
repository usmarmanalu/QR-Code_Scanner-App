package com.dicoding.qrcodescannerapp.data.repository

import com.dicoding.qrcodescannerapp.data.local.*
import kotlinx.coroutines.flow.*
import javax.inject.*


//scanned
@Singleton
class ScanHistoryRepository @Inject constructor(
    private val scanHistoryDao: ScanHistoryDao
) {

    suspend fun insert(scanHistory: ScanHistoryEntity) {
        scanHistoryDao.insert(scanHistory)
    }

    fun getAllScanHistory(): Flow<List<ScanHistoryEntity>> {
        return scanHistoryDao.getAllScanHistory()
    }

    suspend fun deleteScanHistory(scanHistory: ScanHistoryEntity) {
        return scanHistoryDao.delete(scanHistory)
    }
}