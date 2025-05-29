package com.postopia.data.repository

import com.postopia.data.model.Result
import com.postopia.data.model.SpaceVoteInfo
import com.postopia.data.remote.VoteRemoteDataSource
import com.postopia.domain.repository.VoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: VoteRemoteDataSource
) : VoteRepository {
    override suspend fun getSpaceVotes(spaceId: Long): Flow<Result<List<SpaceVoteInfo>>> = flow{
        emit(Result.Loading)
        try {
            val response = remoteDataSource.getSpaceVote(spaceId.toInt())
            if (response.isSuccessful()) {
                emit(Result.Success(response.requireData()))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

}