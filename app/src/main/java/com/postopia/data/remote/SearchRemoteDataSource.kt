package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.PageData
import com.postopia.data.remote.dto.CommentDoc
import com.postopia.data.remote.dto.PostDoc
import com.postopia.data.remote.dto.SpaceDoc
import com.postopia.data.remote.dto.UserDoc
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRemoteDataSource {

    @GET("/search/space")
    suspend fun searchSpace(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int? = null
    ): ApiResponse<PageData<SpaceDoc>>

    @GET("/search/user")
    suspend fun searchUser(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int? = null
    ): ApiResponse<PageData<UserDoc>>

    @GET("/search/post")
    suspend fun searchPost(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int? = null
    ): ApiResponse<PageData<PostDoc>>

    @GET("/search/comment")
    suspend fun searchComment(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int? = null
    ): ApiResponse<PageData<CommentDoc>>


}