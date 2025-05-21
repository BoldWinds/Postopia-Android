package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.PageData
import com.postopia.data.model.Space
import com.postopia.data.model.SpaceAvatar
import com.postopia.data.model.SpacePart
import com.postopia.data.model.User
import com.postopia.data.remote.dto.CreateSpaceRequest
import com.postopia.data.remote.dto.JoinSpaceRequest
import com.postopia.data.remote.dto.LeaveSpaceRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SpaceRemoteDataSource {
    @POST("space/create")
    suspend fun createSpace(@Body request: CreateSpaceRequest) : ApiResponse<Int>

    @POST("space/join")
    suspend fun joinSpace(@Body request: JoinSpaceRequest) : ApiResponse<Unit>

    @POST("space/leave")
    suspend fun leaveSpace(@Body request: LeaveSpaceRequest) : ApiResponse<Unit>

    @GET("space/info")
    suspend fun getSpaceInfo(@Query("id") id: Int) : ApiResponse<Space>

    @GET("space/search/infos")
    suspend fun searchSpaceInfos(@Query("ids") ids: List<Int>) : ApiResponse<SpacePart>

    @GET("space/avatars")
    suspend fun getSpaceAvatars(@Query("ids") ids: List<Int>) : ApiResponse<List<SpaceAvatar>>

    @GET("space/popular")
    suspend fun getPopularSpaces(
        @Query("page") page: String,
        @Query("size") size: String? = null
    ) : ApiResponse<PageData<List<Space>>>

    @GET("space/user")
    suspend fun getUserSpace(
        @Query("page") page: String,
        @Query("size") size: String? = null,
        @Query("userId") userId: Int? = null
    ) : ApiResponse<List<Space>>

    @GET("space/user/prefix")
    suspend fun getSpaceUserByPrefix(
        @Query("prefix") prefix: String,
        @Query("spaceId") spaceId: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
    ) : ApiResponse<List<User>>
}

