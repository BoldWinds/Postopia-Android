package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.SpaceVoteInfo
import retrofit2.http.POST
import retrofit2.http.Query

interface VoteRemoteDataSource {

    @POST("/vote/space")
    suspend fun getSpaceVote(@Query ("spaceId") spaceId: Int): ApiResponse<List<SpaceVoteInfo>>
}

