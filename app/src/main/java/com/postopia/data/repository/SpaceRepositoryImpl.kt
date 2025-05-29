package com.postopia.data.repository

import com.postopia.data.model.Result
import com.postopia.data.model.SpaceInfo
import com.postopia.data.remote.SpaceRemoteDataSource
import com.postopia.data.remote.dto.JoinSpaceRequest
import com.postopia.data.remote.dto.LeaveSpaceRequest
import com.postopia.domain.repository.SpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpaceRemoteDataSource
) : SpaceRepository{
    override suspend fun getPopularSpaces(page : Int): Flow<Result<List<SpaceInfo>>> = flow {
        emit(Result.Loading)
        try{
            val response = remoteDataSource.getPopularSpaces(page.toString())
            if(response.isSuccessful()){
                emit(Result.Success(response.requireData().requireData()))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e : Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getUserSpaces(page : Int): Flow<Result<List<SpaceInfo>>> = flow {
        emit(Result.Loading)
        try{
            val response = remoteDataSource.getUserSpace(page.toString())
            if(response.isSuccessful()){
                val spaceInfos = response.requireData().requireData().map { spacePart ->
                    SpaceInfo(space = spacePart, isMember = true)
                }
                emit(Result.Success(spaceInfos))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e : Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getSpace(spaceID: Long): Flow<Result<SpaceInfo>> = flow {
        emit(Result.Loading)
        try{
            val response = remoteDataSource.getSpaceInfo(spaceID.toInt())
            if(response.isSuccessful()){
                emit(Result.Success(response.requireData()))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e : Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun joinSpace(spaceID: Long): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = JoinSpaceRequest(spaceID.toString())
            val response = remoteDataSource.joinSpace(request)
            if (response.isSuccessful()){
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun leaveSpace(spaceID: Long): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = LeaveSpaceRequest(spaceID.toString())
            val response = remoteDataSource.leaveSpace(request)
            if (response.isSuccessful()){
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }
}

