package com.postopia.data.repository

import com.postopia.data.model.Message
import com.postopia.data.model.Result
import com.postopia.data.remote.MessageRemoteDataSource
import com.postopia.data.remote.dto.DeleteMessagesRequest
import com.postopia.data.remote.dto.ReadMessagesRequest
import com.postopia.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageRemoteDataSource: MessageRemoteDataSource
) : MessageRepository {
    override suspend fun getMessages(
        page: Int,
        size: String?
    ): Flow<Result<List<Message>>> = flow {
        emit(Result.Loading)
        try {
            val response = messageRemoteDataSource.getUserMessages(page, size)
            if(response.isSuccessful()){
                emit(Result.Success(response.requireData().requireData()))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun readMessages(ids: List<Long>): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = ReadMessagesRequest(ids)
            val response = messageRemoteDataSource.readMessages(request)
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun readAllMessages(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val response = messageRemoteDataSource.readAllMessages()
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun deleteMessages(ids: List<Long>): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = DeleteMessagesRequest(ids)
            val response = messageRemoteDataSource.deleteMessages(request)
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun deleteReadMessages(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val response = messageRemoteDataSource.deleteReadMessages()
            if(response.isSuccessful()){
                emit(Result.Success(Unit))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun getUnreadMessageCount(): Flow<Result<Long>> = flow {
        emit(Result.Loading)
        try {
            val response = messageRemoteDataSource.getUnreadMessageCount()
            if(response.isSuccessful()){
                emit(Result.Success(response.requireData()))
            }else{
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

}