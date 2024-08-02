package com.dicoding.qrcodescannerapp.util

import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}
