package com.postopia.data.repository

import com.postopia.data.local.AuthLocalDataSource
import com.postopia.data.model.Result
import com.postopia.data.remote.OpinionRemoteDataSource
import com.postopia.data.remote.dto.CancelOpinionRequest
import com.postopia.data.remote.dto.PostCommentRequest
import com.postopia.data.remote.dto.PostOpinionRequest
import com.postopia.data.remote.dto.VoteOpinionRequest
import com.postopia.domain.repository.OpinionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpinionRepositoryImpl @Inject constructor(
    private val authLocalDataSource : AuthLocalDataSource,
    private val opinionRemoteDataSource: OpinionRemoteDataSource,
) : OpinionRepository {

    override suspend fun updatePostOpinion(
        postId: Long,
        spaceId: Long,
        isPositive: Boolean
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try{
            val userId = authLocalDataSource.getUserId().firstOrNull() ?: throw Exception("User not logged in")
            val request = PostOpinionRequest(postId = postId, spaceId = spaceId, userId = userId, isPositive = isPositive)
            val response = opinionRemoteDataSource.postOpinion(request)
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun cancelPostOpinion(
        postId: Long,
        isPositive: Boolean
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try{
            val request = CancelOpinionRequest(id = postId, isPositive = isPositive)
            val response = opinionRemoteDataSource.cancelPostOpinion(request)
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun updateCommentOpinion(
        commentId: Long,
        spaceId: Long,
        isPositive: Boolean
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try{
            val userId = authLocalDataSource.getUserId().firstOrNull() ?: throw Exception("User not logged in")
            val request = PostCommentRequest(commentId = commentId, spaceId = spaceId, userId = userId, isPositive = isPositive)
            val response = opinionRemoteDataSource.postCommentOpinion(request)
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun cancelCommentOpinion(
        commentId: Long,
        isPositive: Boolean
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try{
            val request = CancelOpinionRequest(id = commentId, isPositive = isPositive)
            val response = opinionRemoteDataSource.cancelCommentOpinion(request)
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun voteOpinion(
        isCommon: Boolean,
        id: Long,
        isPositive: Boolean
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VoteOpinionRequest(id = id, isPositive = isPositive)
            val response = opinionRemoteDataSource.voteOpinion(isCommon, request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }
}
