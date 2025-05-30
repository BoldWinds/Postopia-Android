package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.OpinionPostInfo
import com.postopia.data.model.PageData
import com.postopia.data.model.PostInfo
import com.postopia.data.model.UserPostInfo
import com.postopia.data.remote.dto.CreatePostRequest
import com.postopia.data.remote.dto.UpdatePostRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostRemoteDataSource {

    @POST("/post/create")
    suspend fun createPost(@Body body: CreatePostRequest): ApiResponse<Unit>

    @POST("/post/update")
    suspend fun updatePost(@Body body: UpdatePostRequest): ApiResponse<Unit>

    @GET("/post/info")
    suspend fun getPostInfo(@Query("postId") id: Int): ApiResponse<PostInfo>

    @GET("/post/space")
    suspend fun getPostsBySpace(
        @Query("spaceId") spaceId: Long,
        @Query("page") page: Int,
        @Query("size") size: String? = null,
        @Query("direction") direction: String? = null
    ): ApiResponse<PageData<PostInfo>>

    @GET("/post/search")
    suspend fun searchPosts(@Query("ids") ids: List<String>?): ApiResponse<List<FeedPostInfo>>

    @GET("/post/popular")
    suspend fun getPopularPosts(
        @Query("page") page: Int,
        @Query("size") size: String? = null
    ): ApiResponse<PageData<FeedPostInfo>>

    @GET("/post/user")
    suspend fun getPostsByUser(
        @Query("page") page: Int,
        @Query("order") order: String? = null,
        @Query("direction") direction: String? = null,
        @Query("size") size: String? = null,
        @Query("userId") userId: String? = null
    ): ApiResponse<PageData<UserPostInfo>>

    @GET("/post/user/opinion")
    suspend fun getPostsByOpinion(
        @Query("page") page: Int,
        @Query("direction") direction: String? = null,
        @Query("size") size: String? = null,
        @Query("userid") userId: Long? = null,
        @Query("opinion") opinion: String? = null
    ): ApiResponse<PageData<OpinionPostInfo>>
}
