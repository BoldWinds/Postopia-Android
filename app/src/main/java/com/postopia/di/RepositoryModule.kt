package com.postopia.di

import com.postopia.data.repository.AuthRepositoryImpl
import com.postopia.data.repository.CommentRepositoryImpl
import com.postopia.data.repository.MessageRepositoryImpl
import com.postopia.data.repository.OpinionRepositoryImpl
import com.postopia.data.repository.PostRepositoryImpl
import com.postopia.data.repository.SearchRepositoryImpl
import com.postopia.data.repository.SpaceRepositoryImpl
import com.postopia.data.repository.UserRepositoryImpl
import com.postopia.data.repository.VoteRepositoryImpl
import com.postopia.domain.repository.AuthRepository
import com.postopia.domain.repository.CommentRepository
import com.postopia.domain.repository.MessageRepository
import com.postopia.domain.repository.OpinionRepository
import com.postopia.domain.repository.PostRepository
import com.postopia.domain.repository.SearchRepository
import com.postopia.domain.repository.SpaceRepository
import com.postopia.domain.repository.UserRepository
import com.postopia.domain.repository.VoteRepository
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

    @Binds
    abstract fun bindVoteRepository (
        voteRepositoryImpl: VoteRepositoryImpl
    ) : VoteRepository

    @Binds
    abstract fun bindOpinionRepository(
        opinionRepositoryImpl: OpinionRepositoryImpl
    ): OpinionRepository

    @Binds
    abstract fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository
}