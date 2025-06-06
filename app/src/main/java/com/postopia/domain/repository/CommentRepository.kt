package com.postopia.domain.repository

import com.postopia.data.model.RecursiveCommentInfo
import com.postopia.data.model.Result
import com.postopia.data.model.GeneralCommentInfo
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun getPostComments(postId : Long, page: Int = 0) : Flow<Result<List<RecursiveCommentInfo>>>

    suspend fun replyComment(
        postId: Long,
        content: String,
        spaceId: Long,
        userId: Long,
        parentId: Long? = null,
    ): Flow<Result<Unit>>

    suspend fun getUserComments(
        page: Int = 0,
        userId: Long? = null,
        direction: String? = null,
        size: String? = null
    ): Flow<Result<List<GeneralCommentInfo>>>
}