package com.postopia.domain.repository

import com.postopia.data.model.RecursiveCommentInfo
import com.postopia.data.model.Result
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun getPostComments(postId : Long, page: Int = 0) : Flow<Result<List<RecursiveCommentInfo>>>
}