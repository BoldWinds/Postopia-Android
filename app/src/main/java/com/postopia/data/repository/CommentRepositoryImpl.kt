package com.postopia.data.repository

import com.postopia.data.model.RecursiveCommentInfo
import com.postopia.data.model.Result
import com.postopia.data.remote.CommentRemoteDataSource
import com.postopia.data.remote.dto.CreateCommentRequest
import com.postopia.domain.repository.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentRemoteDataSource: CommentRemoteDataSource
) : CommentRepository {

    override suspend fun getPostComments(
        postId: Long,
        page: Int
    ): Flow<Result<List<RecursiveCommentInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = commentRemoteDataSource.getPostComments(postId, page)
            if (response.isSuccessful()) {
                emit(Result.Success(response.requireData().requireData()))
            }else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun replyComment(
        postId: Long,
        content: String,
        spaceId: Long,
        userId: Long,
        parentId: Long?
    ): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        try {
            val request = CreateCommentRequest(
                postId = postId,
                content = content,
                spaceId = spaceId,
                userId = userId,
                parentId = parentId
            )
            val response = commentRemoteDataSource.createComment(request)
            if (response.isSuccessful()) {
                emit(Result.Success(Unit))
            }else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

}