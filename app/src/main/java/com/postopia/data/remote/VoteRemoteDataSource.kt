package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.SpaceVoteInfo
import com.postopia.data.remote.dto.VoteCommentRequest
import com.postopia.data.remote.dto.VotePostRequest
import com.postopia.data.remote.dto.VoteUserRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface VoteRemoteDataSource {

    @POST("/vote/space")
    suspend fun getSpaceVote(@Query ("spaceId") spaceId: Int): ApiResponse<List<SpaceVoteInfo>>

    @POST("/vote/comment-delete")
    suspend fun deleteCommentVote(@Body request: VoteCommentRequest): ApiResponse<Unit>

    @POST("/vote/comment-pin")
    suspend fun pinCommentVote(@Body request: VoteCommentRequest): ApiResponse<Unit>

    @POST("/vote/comment-unpin")
    suspend fun unpinCommentVote(@Body request: VoteCommentRequest): ApiResponse<Unit>

    @POST("/vote/post-delete")
    suspend fun deletePostVote(@Body request: VotePostRequest): ApiResponse<Unit>

    @POST("/vote/post-archive")
    suspend fun archivePostVote(@Body request: VotePostRequest): ApiResponse<Unit>

    @POST("/vote/post-unarchive")
    suspend fun unarchivePostVote(@Body request: VotePostRequest): ApiResponse<Unit>

    @POST("/vote/user-mute")
    suspend fun muteUserVote(@Body request: VoteUserRequest): ApiResponse<Unit>

    @POST("/vote/user-expel")
    suspend fun expelUserVote(@Body request: VoteUserRequest): ApiResponse<Unit>
}

