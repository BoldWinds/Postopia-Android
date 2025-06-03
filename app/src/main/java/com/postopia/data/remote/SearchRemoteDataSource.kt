package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.PageData
import com.postopia.data.remote.dto.SpaceDoc
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
    ): ApiResponse<PageData<SpaceDoc>>

    @GET("/search/post")
    suspend fun searchPost(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int? = null
    ): ApiResponse<PageData<SpaceDoc>>

    @GET("/search/comment")
    suspend fun searchComment(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int? = null
    ): ApiResponse<PageData<SpaceDoc>>


}