package com.dicoding.qrcodescannerapp.data.local

import android.content.*
import androidx.room.*

@Database(
    entities = [ScanHistoryEntity::class, GenerateTextEntity::class, GenerateWebEntity::class, GenerateWifiEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanHistoryDao(): ScanHistoryDao

    abstract fun generateTextHistoryDao(): GenerateTextDao
    abstract fun generateWebHistoryDao(): GenerateWebDao
    abstract fun generateWifiHistoryDao(): GenerateWifiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}