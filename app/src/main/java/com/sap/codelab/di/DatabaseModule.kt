package com.sap.codelab.di

import android.content.Context
import androidx.room.Room
import com.sap.codelab.repository.MemoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

import com.sap.codelab.repository.MemoDatabase
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MemoDatabase {
        return Room.databaseBuilder(
            appContext,
            MemoDatabase::class.java,
            "memo_database"
        ).build()
    }

    @Provides
    fun provideMemoDao(database: MemoDatabase): MemoDao {
        return database.getMemoDao()
    }
}