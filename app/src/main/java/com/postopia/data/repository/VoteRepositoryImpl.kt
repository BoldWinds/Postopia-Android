package com.postopia.data.repository

import com.postopia.data.model.Result
import com.postopia.data.model.SpaceVoteInfo
import com.postopia.data.remote.VoteRemoteDataSource
import com.postopia.data.remote.dto.VoteCommentRequest
import com.postopia.data.remote.dto.VotePostRequest
import com.postopia.data.remote.dto.VoteUserRequest
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

    override suspend fun deleteComment(
        postId: Long,
        userId: Long,
        spaceId: Long,
        commentId: Long,
        commentContent: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VoteCommentRequest(
                postId = postId,
                userId = userId,
                spaceId = spaceId,
                commentId = commentId,
                commentContent = commentContent
            )
            val response = remoteDataSource.deleteCommentVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun pinComment(
        postId: Long,
        userId: Long,
        spaceId: Long,
        commentId: Long,
        commentContent: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VoteCommentRequest(
                postId = postId,
                userId = userId,
                spaceId = spaceId,
                commentId = commentId,
                commentContent = commentContent
            )
            val response = remoteDataSource.pinCommentVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun unpinComment(
        postId: Long,
        userId: Long,
        spaceId: Long,
        commentId: Long,
        commentContent: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VoteCommentRequest(
                postId = postId,
                userId = userId,
                spaceId = spaceId,
                commentId = commentId,
                commentContent = commentContent
            )
            val response = remoteDataSource.unpinCommentVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun deletePost(
        postId: Long,
        userId: Long,
        spaceId: Long,
        postSubject: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VotePostRequest(
                postId = postId,
                userId = userId,
                spaceId = spaceId,
                postSubject = postSubject
            )
            val response = remoteDataSource.deletePostVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun archivePost(
        postId: Long,
        userId: Long,
        spaceId: Long,
        postSubject: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VotePostRequest(
                postId = postId,
                userId = userId,
                spaceId = spaceId,
                postSubject = postSubject
            )
            val response = remoteDataSource.archivePostVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun unarchivePost(
        postId: Long,
        userId: Long,
        spaceId: Long,
        postSubject: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VotePostRequest(
                postId = postId,
                userId = userId,
                spaceId = spaceId,
                postSubject = postSubject
            )
            val response = remoteDataSource.unarchivePostVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun muteUser(
        spaceId: Long,
        userId: Long,
        username: String,
        reason: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VoteUserRequest(
                spaceId = spaceId,
                userId = userId,
                username = username,
                reason = reason
            )
            val response = remoteDataSource.muteUserVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun expelUser(
        spaceId: Long,
        userId: Long,
        username: String,
        reason: String
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = VoteUserRequest(
                spaceId = spaceId,
                userId = userId,
                username = username,
                reason = reason
            )
            val response = remoteDataSource.expelUserVote(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

}