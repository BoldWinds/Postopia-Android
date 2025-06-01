package com.postopia.domain.repository

import com.postopia.data.model.Message
import com.postopia.data.model.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun getMessages(page: Int = 0, size: String? = null): Flow<Result<List<Message>>>

    suspend fun readMessages(ids: List<Long>): Flow<Result<Unit>>

    suspend fun readAllMessages(): Flow<Result<Unit>>

    suspend fun deleteMessages(ids: List<Long>): Flow<Result<Unit>>

    suspend fun deleteReadMessages(): Flow<Result<Unit>>

    suspend fun getUnreadMessageCount(): Flow<Result<Long>>
}