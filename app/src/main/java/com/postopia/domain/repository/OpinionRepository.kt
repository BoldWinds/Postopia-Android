package com.postopia.domain.repository

import com.postopia.data.model.Result
import kotlinx.coroutines.flow.Flow

interface OpinionRepository {

    suspend fun updatePostOpinion(postId: Long, spaceId: Long, isPositive: Boolean): Flow<Result<Unit>>

    suspend fun cancelPostOpinion(postId: Long, isPositive: Boolean): Flow<Result<Unit>>

    suspend fun updateCommentOpinion(commentId: Long, spaceId: Long, isPositive: Boolean): Flow<Result<Unit>>

    suspend fun cancelCommentOpinion(commentId: Long, isPositive: Boolean): Flow<Result<Unit>>
}