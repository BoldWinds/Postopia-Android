package com.postopia.data.repository

import com.postopia.data.model.RecursiveCommentInfo
import com.postopia.data.model.Result
import com.postopia.data.remote.CommentRemoteDataSource
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

}