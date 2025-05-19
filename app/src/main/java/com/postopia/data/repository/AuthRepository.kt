package com.postopia.data.repository

import com.postopia.data.model.Result
import com.postopia.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(username: String, password: String): Flow<Result<Nothing>>

    suspend fun login(username : String, password: String): Flow<Result<User>>
}