package com.sap.codelab.di

import android.content.Context
import androidx.room.Room
import com.sap.codelab.data.database.MemoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.sap.codelab.data.database.MemoDatabase
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    const val DATABASE_NAME = "memo_database"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MemoDatabase {
        return Room.databaseBuilder(
            appContext,
            MemoDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideMemoDao(database: MemoDatabase): MemoDao {
        return database.getMemoDao()
    }

}