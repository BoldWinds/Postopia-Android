package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.OpinionCommentInfo
import com.postopia.data.model.PageData
import com.postopia.data.model.RecursiveCommentInfo
import com.postopia.data.model.SearchCommentInfo
import com.postopia.data.model.UserCommentInfo
import com.postopia.data.remote.dto.CreateCommentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentRemoteDataSource {
    @POST("/comment/create")
    suspend fun createComment(@Body request: CreateCommentRequest): ApiResponse<Long>

    @GET("/comment/post")
    suspend fun getPostComments(@Query("postId") postId: Long, @Query("page") page: Int = 0, ): ApiResponse<PageData<RecursiveCommentInfo>>

    @GET("/comment/search")
    suspend fun searchComments(@Query("ids") ids: List<String>?): ApiResponse<List<SearchCommentInfo>>

    @GET("/comment/user")
    suspend fun getUserComments(
        @Query("page") page: Int,
        @Query("direction") direction: String? = null,
        @Query("size") size: String? = null,
        @Query("userid") userId: String? = null
    ): ApiResponse<PageData<UserCommentInfo>>

    @GET("/comment/user/opinion")
    suspend fun getUserOpinionComments(
        @Query("page") page: Int,
        @Query("direction") direction: String? = null,
        @Query("size") size: String? = null,
        @Query("userid") userId: String? = null,
        @Query("opinion") opinion: String? = null
    ): ApiResponse<PageData<OpinionCommentInfo>>
}
