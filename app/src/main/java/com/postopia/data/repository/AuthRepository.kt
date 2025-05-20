package com.postopia.data.repository

import com.postopia.data.model.Credential
import com.postopia.data.model.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(username: String, password: String): Flow<Result<Unit>>

    suspend fun login(username : String, password: String): Flow<Result<Credential>>

    suspend fun refresh(refreshToken : String)  : Flow<Result<String>>
}