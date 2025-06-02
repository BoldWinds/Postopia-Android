package com.postopia.data.repository

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.PostInfo
import com.postopia.data.model.Result
import com.postopia.data.model.UserPostInfo
import com.postopia.data.remote.PostRemoteDataSource
import com.postopia.data.remote.dto.CreatePostRequest
import com.postopia.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val remoteDataSource: PostRemoteDataSource
) : PostRepository {

    override suspend fun getUserPosts(page: Int, userId : String?): Flow<Result<List<UserPostInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getPostsByUser(page, userId = userId)
            if (response.isSuccessful()) {
                emit(Result.Success(response.requireData().requireData()))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
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

    override suspend fun getSpacePosts(
        spaceId: Long,
        page: Int,
        size: String?
    ): Flow<Result<List<PostInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getPostsBySpace(spaceId, page, size)
            if (response.isSuccessful()) {
                emit(Result.Success(response.requireData().requireData()))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getPostByID(postID: Long): Flow<Result<PostInfo>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getPostInfo(postID.toInt())
            if(response.isSuccessful()){
                emit(Result.Success(response.requireData()))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun createPost(
        spaceId: Long,
        spaceName: String,
        subject: String,
        content: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = CreatePostRequest(
                spaceId = spaceId,
                spaceName = spaceName,
                subject = subject,
                content = content
            )
            val response = remoteDataSource.createPost(request)
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception) {
            emit(Result.Error(e))
        }
    }
}

