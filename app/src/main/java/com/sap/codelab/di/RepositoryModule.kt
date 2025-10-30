package com.sap.codelab.di

import com.sap.codelab.repository.IMemoRepository
import com.sap.codelab.repository.IMomoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMemoRepository(memoRepositoryImpl: IMomoRepositoryImpl): IMemoRepository
}