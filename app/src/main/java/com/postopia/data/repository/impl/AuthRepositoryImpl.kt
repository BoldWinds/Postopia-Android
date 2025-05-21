package com.postopia.data.repository.impl

import com.postopia.data.local.AuthLocalDataSource
import com.postopia.data.model.Credential
import com.postopia.data.model.Result
import com.postopia.data.remote.AuthRemoteDataSource
import com.postopia.data.remote.dto.LoginRequest
import com.postopia.data.remote.dto.RefreshTokenRequest
import com.postopia.data.remote.dto.RegisterRequest
import com.postopia.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    override suspend fun register(username: String, password: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = RegisterRequest(username, password)
            val response = remoteDataSource.register(request)

            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun login(username: String, password: String): Flow<Result<Credential>> = flow {
        emit(Result.Loading)
        try {
            val request = LoginRequest(username, password)
            val response = remoteDataSource.login(request)

            if (response.isSuccessful() && response.data != null) {
                val credential = response.requireData()
                localDataSource.saveCredential(credential)
                emit(Result.Success(credential))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun refresh(refreshToken: String): Flow<Result<String>>  = flow {
        emit(Result.Loading)
        try{
            val request = RefreshTokenRequest(refreshToken)
            val response = remoteDataSource.refresh(request)

            if(response.isSuccessful()  && response.data != null){
                val token = response.data
                localDataSource.saveAccessToken(token)
                emit(Result.Success(token))
            }else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }
}

