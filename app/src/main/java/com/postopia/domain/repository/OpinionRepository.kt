package com.postopia.domain.repository

import com.postopia.data.model.Result
import kotlinx.coroutines.flow.Flow

interface OpinionRepository {

    suspend fun updateOpinionStatus(postId: Long, spaceId: Long, isPositive: Boolean): Flow<Result<Unit>>

    suspend fun cancelPostOpinion(postId: Long, isPositive: Boolean): Flow<Result<Unit>>
}