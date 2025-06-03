package com.postopia.data.repository

import com.postopia.data.model.GeneralCommentInfo
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

    override suspend fun getUserComments(
        page: Int,
        userId: Long?,
        direction: String?,
        size: String?
    ): Flow<Result<List<GeneralCommentInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = commentRemoteDataSource.getUserComments(
                page = page,
                userId = userId?.toString(),
                direction = direction,
                size = size
            )
            if (response.isSuccessful()) {
                val userCommentInfoList = response.requireData().requireData()
                val ids = userCommentInfoList.map { it.comment.id.toString() }
                val searchResponse =  commentRemoteDataSource.searchComments(ids)
                if(searchResponse.isSuccessful()){
                    val searchCommentInfoList = searchResponse.requireData()
                    val data = userCommentInfoList.mapIndexed { index, userCommentInfo ->
                        // 找到对应的searchCommentInfo
                        val searchCommentInfo = searchCommentInfoList.find {
                            it.comment.id == userCommentInfo.comment.id
                        }
                        if (searchCommentInfo == null){
                            emit(Result.Error(Exception("Search comment info not found for comment ID: ${userCommentInfo.comment.id}")))
                            return@flow
                        }else{
                            GeneralCommentInfo(
                                commentId = userCommentInfo.comment.id,
                                spaceId = userCommentInfo.comment.spaceId,
                                spaceName = searchCommentInfo.post.spaceName,
                                postId = userCommentInfo.comment.postId,
                                postSubject = searchCommentInfo.post.subject,
                                authorId = userCommentInfo.comment.userId,
                                authorName = searchCommentInfo.user.nickname,
                                authorAvatar = searchCommentInfo.user.avatar,
                                createdAt = userCommentInfo.comment.createdAt,
                                content = userCommentInfo.comment.content
                            )
                        }
                    }
                    emit(Result.Success(data))
                }else{
                    emit(Result.Error(Exception(searchResponse.message)))
                }
            }else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

}

