package com.postopia.domain.repository

import com.postopia.data.model.Result
import com.postopia.data.model.SpaceVoteInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface VoteRepository{

    @GET("/vote/space")
    suspend fun getSpaceVotes(spaceId: Long): Flow<Result<List<SpaceVoteInfo>>>
}