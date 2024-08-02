package com.dicoding.qrcodescannerapp.scanner

sealed class ScannerViewState {
    data object Loading : ScannerViewState()
    data object Success : ScannerViewState()
    data object Error : ScannerViewState()
}