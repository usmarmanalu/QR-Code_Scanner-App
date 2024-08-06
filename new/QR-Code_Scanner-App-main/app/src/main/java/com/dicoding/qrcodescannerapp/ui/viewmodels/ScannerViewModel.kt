package com.dicoding.qrcodescannerapp.ui.viewmodels

import android.content.*
import androidx.camera.core.*
import androidx.camera.view.*
import androidx.lifecycle.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.data.repository.*
import com.dicoding.qrcodescannerapp.scanner.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

@HiltViewModel
class ScannerViewModel @Inject constructor(

    private val scanHistoryRepository: ScanHistoryRepository

) : ViewModel() {

    private lateinit var qrCodeManager: ScannerManager

    /**
     * Initialize Camera Manager class.
     */
    internal fun startCamera(
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        previewView: PreviewView,
        onResult: (state: ScannerViewState, result: String) -> Unit,
    ) {
        qrCodeManager = ScannerManager(
            owner = viewLifecycleOwner, context = context,
            viewPreview = previewView,
            onResult = { state, result ->
                onResult(state, result)
            },
            lensFacing = CameraSelector.LENS_FACING_BACK
        )
    }

    fun enableFlashForQrCode(enabled: Boolean) {
        qrCodeManager.enableFlashForQrCode(enabled)
    }

    fun enableFlashForCamera(enabled: Boolean) {
        qrCodeManager.enableFlashForCamera(enabled)
    }

    val scanHistories: LiveData<List<ScanHistoryEntity>> =
        scanHistoryRepository.getAllScanHistory().asLiveData()
            .map { it.sortedByDescending { scanHistory -> scanHistory.timestamp } }

    fun insertScanHistory(result: String) {
        val scanHistory = ScanHistoryEntity(0, result, System.currentTimeMillis())
        viewModelScope.launch {
            scanHistoryRepository.insert(scanHistory)
        }
    }

    fun insertScanHistoryUndo(scanHistory: ScanHistoryEntity) {
        viewModelScope.launch {
            scanHistoryRepository.insert(scanHistory)
        }
    }

    fun deleteScanHistory(scanHistory: ScanHistoryEntity) {
        viewModelScope.launch {
            scanHistoryRepository.deleteScanHistory(scanHistory)
        }
    }
}