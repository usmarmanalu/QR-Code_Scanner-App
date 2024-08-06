package com.dicoding.qrcodescannerapp.scanner

import android.content.*
import androidx.camera.core.*
import androidx.camera.lifecycle.*
import androidx.camera.view.*
import androidx.lifecycle.*
import com.dicoding.qrcodescannerapp.scanner.base.*

class ScannerManager(
    owner: LifecycleOwner,
    context: Context,
    viewPreview: PreviewView,
    private val onResult: (state: ScannerViewState, result: String) -> Unit,
    lensFacing: Int
) : BaseCameraManager(owner, context, viewPreview, lensFacing, {}) {

    private fun getImageAnalysis(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, ScannerAnalyzer(onResult))
            }
    }

    override fun bindToLifecycle(
        cameraProvider: ProcessCameraProvider,
        owner: LifecycleOwner,
        cameraSelector: CameraSelector,
        previewView: Preview,
        imageCapture: ImageCapture
    ) {
        camera = cameraProvider.bindToLifecycle(
            owner,
            cameraSelector,
            previewView,
            getImageAnalysis(),
            imageCapture
        )
    }
}