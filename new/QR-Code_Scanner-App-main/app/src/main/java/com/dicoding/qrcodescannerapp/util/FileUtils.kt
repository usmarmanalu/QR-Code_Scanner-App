package com.dicoding.qrcodescannerapp.util

import android.content.*
import android.graphics.*
import android.net.*
import androidx.core.content.*
import java.io.*

object FileUtils {

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val filesDir = context.filesDir
        val imageFile = File(filesDir, "${System.currentTimeMillis()}.png")

        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    }
}
