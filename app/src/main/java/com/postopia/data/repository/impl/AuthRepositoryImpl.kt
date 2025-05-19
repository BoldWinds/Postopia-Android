package com.postopia.data.repository.impl

import com.postopia.data.local.AuthLocalDataSource
import com.postopia.data.model.Result
import com.postopia.data.model.User
import com.postopia.data.remote.AuthRemoteDataSource
import com.postopia.data.remote.dto.LoginRequest
import com.postopia.data.remote.dto.RegisterRequest
import com.postopia.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    override suspend fun register(username: String, password: String): Flow<Result<Nothing>> = flow {
        emit(Result.Loading)
        try {
            val request = RegisterRequest(username, password)
            val response = remoteDataSource.register(request)

            if (response.isSuccessful()) {
                emit(Result.Success(Unit as Nothing))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun login(username: String, password: String): Flow<Result<User>> = flow {
        emit(Result.Loading)
        try {
            val request = LoginRequest(username, password)
            val response = remoteDataSource.login(request)

            if (response.isSuccessful() && response.data != null) {
                // 保存用户名和token
                val user = User(
                        username = username,
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken
                    )
                localDataSource.saveUser(user)
                emit(Result.Success(user))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}

