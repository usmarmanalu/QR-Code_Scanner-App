package com.dicoding.qrcodescannerapp.scanner

import android.annotation.*
import androidx.camera.core.*
import com.google.mlkit.vision.barcode.*
import com.google.mlkit.vision.barcode.common.*
import com.google.mlkit.vision.common.*
import kotlinx.coroutines.*

class ScannerAnalyzer(
    private val onResult: (state: ScannerViewState, barcode: String) -> Unit
) : ImageAnalysis.Analyzer {

    private val delayForProcessingNextImage = 300L

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val options =
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()
        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                .let { image ->
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                onResult(ScannerViewState.Success, barcode.rawValue ?: "")
                            }
                        }
                        .addOnFailureListener {
                            onResult(ScannerViewState.Error, it.message.toString())
                        }
                        .addOnCompleteListener {
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(delayForProcessingNextImage)
                                imageProxy.close()
                            }
                        }
                }
        } else {
            onResult(ScannerViewState.Error, "Image is empty")
        }
    }
}