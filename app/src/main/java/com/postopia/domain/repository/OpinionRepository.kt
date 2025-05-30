package com.postopia.domain.repository

import com.postopia.data.model.Result
import kotlinx.coroutines.flow.Flow

interface OpinionRepository {

    suspend fun postPositiveOpinion(postId: Long, spaceId: Long): Flow<Result<Unit>>

    suspend fun postNegativeOpinion(postId: Long, spaceId: Long): Flow<Result<Unit>>
}