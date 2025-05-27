package com.postopia.data.repository

import com.postopia.data.model.Result
import com.postopia.data.model.UserDetail
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    suspend fun getCurrentUser() : Flow<Result<UserDetail>>
}