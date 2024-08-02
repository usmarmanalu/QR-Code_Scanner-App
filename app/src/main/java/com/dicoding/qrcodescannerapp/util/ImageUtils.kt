package com.dicoding.qrcodescannerapp.util

import android.content.*
import android.graphics.*
import android.os.*
import android.provider.*
import android.widget.*
import com.dicoding.qrcodescannerapp.*

object ImageUtils {

    fun saveQRCodeToGallery(context: Context, bitmap: Bitmap) {
        val filename = "QRCode_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QR Codes")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.qr_code_saved_to_gallery), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.failed_to_save_qr_code), Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: run {
                Toast.makeText(
                    context,
                    context.getString(R.string.failed_to_open_output_stream), Toast.LENGTH_SHORT
                ).show()
            }
        } ?: run {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_get_uri), Toast.LENGTH_SHORT
            ).show()
        }
    }
}
