package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.remote.dto.CancelOpinionRequest
import com.postopia.data.remote.dto.PostCommentRequest
import com.postopia.data.remote.dto.PostOpinionRequest
import com.postopia.data.remote.dto.VoteOpinionRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface OpinionRemoteDataSource{

    @POST("/opinion/post")
    suspend fun postOpinion(@Body body: PostOpinionRequest): ApiResponse<Unit>

    @POST("/opinion/post/delete")
    suspend fun cancelPostOpinion(@Body body: CancelOpinionRequest): ApiResponse<Unit>

    @POST("/opinion/comment")
    suspend fun postCommentOpinion(@Body body: PostCommentRequest): ApiResponse<Unit>

    @POST("/opinion/comment/delete")
    suspend fun cancelCommentOpinion(@Body body: CancelOpinionRequest): ApiResponse<Unit>

    @POST("/opinion/vote")
    suspend fun voteOpinion(@Query ("isCommon") isCommon: Boolean, @Body request: VoteOpinionRequest): ApiResponse<Unit>
}