package com.dicoding.qrcodescannerapp.ui.viewmodels

import androidx.lifecycle.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.data.repository.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

//text
@HiltViewModel
class GenerateTextViewModel @Inject constructor(
    private val generateTextRepository: GenerateTextRepository
) : ViewModel() {

    val generateHistories: LiveData<List<GenerateTextEntity>> =
        generateTextRepository.getAllGenerateHistory().asLiveData()
            .map { it.sortedByDescending { history -> history.timestamp } }


    fun insertGenerateHistory(generateTextEntity: GenerateTextEntity) {
        viewModelScope.launch {
            generateTextRepository.insert(generateTextEntity)
        }
    }

    fun deleteGenerateHistory(generateTextEntity: GenerateTextEntity) {
        viewModelScope.launch {
            generateTextRepository.deleteGenerateHistory(generateTextEntity)
        }
    }
}

//web
@HiltViewModel
class GenerateWebViewModel @Inject constructor(
    private val generateWebRepository: GenerateWebRepository
) : ViewModel() {

    val generateHistories: LiveData<List<GenerateWebEntity>> =
        generateWebRepository.getAllGenerateHistory().asLiveData()
            .map { it.sortedByDescending { history -> history.timestamp } }


    fun insertGenerateHistory(generateWebEntity: GenerateWebEntity) {
        viewModelScope.launch {
            generateWebRepository.insert(generateWebEntity)
        }
    }

    fun deleteGenerateHistory(generateWebEntity: GenerateWebEntity) {
        viewModelScope.launch {
            generateWebRepository.deleteGenerateHistory(generateWebEntity)
        }
    }
}

//wifi
@HiltViewModel
class GenerateWifiViewModel @Inject constructor(
    private val generateHistoryWifiRepository: GenerateHistoryWifiRepository
) : ViewModel() {

    val generateWifiHistories: LiveData<List<GenerateWifiEntity>> =
        generateHistoryWifiRepository.getAllGenerateWifiHistory().asLiveData()
            .map { it.sortedByDescending { history -> history.timestamp } }

    fun insertGenerateWifiHistory(generateWifiEntity: GenerateWifiEntity) {
        viewModelScope.launch {
            generateHistoryWifiRepository.insert(generateWifiEntity)
        }
    }

    fun deleteGenerateWifiHistory(generateWifiEntity: GenerateWifiEntity) {
        viewModelScope.launch {
            generateHistoryWifiRepository.deleteGenerateWifiHistory(generateWifiEntity)
        }
    }
}