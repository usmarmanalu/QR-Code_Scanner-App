package com.dicoding.qrcodescannerapp.di.module

import android.content.*
import com.dicoding.qrcodescannerapp.data.local.*
import com.dicoding.qrcodescannerapp.data.repository.*
import com.dicoding.qrcodescannerapp.permission.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //permission
    @Singleton
    @Provides
    fun getPermissionUtil(): PermissionUtil {
        return PermissionUtil()
    }

    //database
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    //scanned
    @Singleton
    @Provides
    fun provideScanHistoryDao(database: AppDatabase): ScanHistoryDao {
        return database.scanHistoryDao()
    }

    @Singleton
    @Provides
    fun provideScanHistoryRepository(dao: ScanHistoryDao): ScanHistoryRepository {
        return ScanHistoryRepository(dao)
    }

    //text
    @Singleton
    @Provides
    fun provideGenerateTextDao(database: AppDatabase): GenerateTextDao {
        return database.generateTextHistoryDao()
    }

    @Singleton
    @Provides
    fun provideGenerateTextRepository(dao: GenerateTextDao): GenerateTextRepository {
        return GenerateTextRepository(dao)
    }

    //web
    @Singleton
    @Provides
    fun provideGenerateWebDao(database: AppDatabase): GenerateWebDao {
        return database.generateWebHistoryDao()
    }

    @Singleton
    @Provides
    fun provideGenerateWebRepository(dao: GenerateWebDao): GenerateWebRepository {
        return GenerateWebRepository(dao)
    }

    //wifi
    @Singleton
    @Provides
    fun provideGenerateWifiHistoryDao(database: AppDatabase): GenerateWifiDao {
        return database.generateWifiHistoryDao()
    }

    @Singleton
    @Provides
    fun provideGenerateWifiHistoryRepository(dao: GenerateWifiDao): GenerateHistoryWifiRepository {
        return GenerateHistoryWifiRepository(dao)
    }

}