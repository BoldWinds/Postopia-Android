package com.postopia.di

import com.postopia.data.repository.AuthRepository
import com.postopia.data.repository.PostRepository
import com.postopia.data.repository.SpaceRepository
import com.postopia.data.repository.UserRepository
import com.postopia.data.repository.impl.AuthRepositoryImpl
import com.postopia.data.repository.impl.PostRepositoryImpl
import com.postopia.data.repository.impl.SpaceRepositoryImpl
import com.postopia.data.repository.impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindSpaceRepository(
        spaceRepositoryImpl: SpaceRepositoryImpl
    ): SpaceRepository

    @Binds
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository
}