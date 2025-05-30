package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.remote.dto.PostOpinionRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface OpinionRemoteDataSource{

    @POST("/opinion/post")
    suspend fun postOpinion(@Body body: PostOpinionRequest): ApiResponse<Unit>
}