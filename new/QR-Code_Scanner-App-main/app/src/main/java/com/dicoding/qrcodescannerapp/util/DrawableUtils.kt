package com.dicoding.qrcodescannerapp.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

object DrawableUtils {

    fun getWhiteBackArrowDrawable(context: Context): Drawable? {
        val drawable =
            ContextCompat.getDrawable(context, androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        drawable?.let {
            DrawableCompat.setTint(it, ContextCompat.getColor(context, android.R.color.white))
        }
        return drawable
    }
}
