package com.postopia.data.repository.impl

import com.postopia.data.local.UserLocalDataSource
import com.postopia.data.model.Result
import com.postopia.data.model.UserDetail
import com.postopia.data.remote.UserRemoteDataSource
import com.postopia.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getCurrentUser(): Flow<Result<UserDetail>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getUserDetail(null)
            if (response.isSuccessful()) {
                val user = response.requireData()
                emit(Result.Success(user))
                localDataSource.cacheUserDetail(user.userId.toString(), user)
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }

    }
}
