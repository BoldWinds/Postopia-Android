package com.postopia.data.repository

import com.postopia.data.local.AuthLocalDataSource
import com.postopia.data.model.Result
import com.postopia.data.remote.OpinionRemoteDataSource
import com.postopia.data.remote.dto.PostOpinionRequest
import com.postopia.domain.repository.OpinionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpinionRepositoryImpl @Inject constructor(
    private val authLocalDataSource : AuthLocalDataSource,
    private val opinionRemoteDataSource: OpinionRemoteDataSource,
) : OpinionRepository {

    override suspend fun postPositiveOpinion(
        postId: Long,
        spaceId: Long
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try{
            val userId = authLocalDataSource.getUserId().firstOrNull() ?: throw Exception("User not logged in")
            val request = PostOpinionRequest(postId = postId, spaceId = spaceId, userId = userId,isPositive = true)
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

    override suspend fun postNegativeOpinion(
        postId: Long,
        spaceId: Long
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try{
            val userId = authLocalDataSource.getUserId().firstOrNull() ?: throw Exception("User not logged in")
            val request = PostOpinionRequest(postId = postId, spaceId = spaceId, userId = userId,isPositive = false)
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
}
