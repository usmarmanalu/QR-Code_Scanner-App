package com.dicoding.qrcodescannerapp.data.local

import androidx.room.*

@Entity(tableName = "generate_text")
data class GenerateTextEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val generate: String,
    val timestamp: Long,
    val imageUri: String? = null,
    val type: HistoryType
)

@Entity(tableName = "generate_web")
data class GenerateWebEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val generate: String,
    val timestamp: Long,
    val imageUri: String? = null,
    val type: HistoryType
)

//wifi
@Entity(tableName = "generate_wifi")
data class GenerateWifiEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long,
    val generateNameWifi: String,
    val generatePasswordWifi: String,
    val imageUri: String? =  null,
    val type: HistoryType
)

enum class HistoryType {
    TEXT, WEBSITE, WIFI, EVENT, CONTACT, BUSINESS, LOCATION, WHATSAPP, EMAIL, TWITTER, INSTAGRAM, TELEPHONE
}