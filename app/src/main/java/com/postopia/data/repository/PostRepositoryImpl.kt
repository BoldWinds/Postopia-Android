package com.postopia.data.repository

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.Result
import com.postopia.data.model.UserPostInfo
import com.postopia.data.remote.PostRemoteDataSource
import com.postopia.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val remoteDataSource: PostRemoteDataSource
) : PostRepository {

    override suspend fun getUserPosts(userId: String): Flow<Result<List<UserPostInfo>>> = flow {
        emit(Result.Loading)
        try {

        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun getPopularPosts(
        page: Int,
        size: String?
    ): Flow<Result<List<FeedPostInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getPopularPosts(page, size)
            if (response.isSuccessful()) {
                emit(Result.Success(response.requireData().requireData()))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }
}