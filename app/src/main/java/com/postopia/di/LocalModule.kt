package com.postopia.di

import com.postopia.data.local.AuthLocalDataSource
import com.postopia.data.local.UserLocalDataSource
import com.postopia.data.local.impl.AuthLocalDataSourceImpl
import com.postopia.data.local.impl.UserLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindUserLocalDataSource(
        userLocalDataSourceImpl: UserLocalDataSourceImpl
    ): UserLocalDataSource
}

