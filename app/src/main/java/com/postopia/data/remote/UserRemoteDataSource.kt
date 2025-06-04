package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.UserAvatar
import com.postopia.data.model.UserDetail
import com.postopia.data.model.UserInfo
import com.postopia.data.remote.dto.UpdateEmailRequest
import com.postopia.data.remote.dto.UpdateEmailShowRequest
import com.postopia.data.remote.dto.UpdateIntroductionRequest
import com.postopia.data.remote.dto.UpdateNicknameRequest
import com.postopia.data.remote.dto.VerifyEmailRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserRemoteDataSource {

    @POST("/user/upload")
    @Multipart
    suspend fun uploadMedia(
        @Query("isVideo") isVideo: Boolean?,
        @Part file: MultipartBody.Part
    ): ApiResponse<String>

    @POST("/user/avatar")
    @Multipart
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): ApiResponse<String>

    @POST("/user/nickname")
    suspend fun updateNickname(@Body body: UpdateNicknameRequest): ApiResponse<Unit>

    @POST("/user/introduction")
    suspend fun updateIntroduction(@Body body: UpdateIntroductionRequest): ApiResponse<Unit>

    @POST("/user/email/show")
    suspend fun updateEmailShow(@Body body: UpdateEmailShowRequest): ApiResponse<Unit>

    @POST("/user/email")
    suspend fun requestChangeEmail(@Body body: UpdateEmailRequest): ApiResponse<Unit>

    @POST("/user/email/verify")
    suspend fun verifyEmail(@Body body: VerifyEmailRequest): ApiResponse<Unit>

    @GET("/user/detail")
    suspend fun getUserDetail(@Query("userId") userId: String?): ApiResponse<UserDetail>

    @GET("/user/search")
    suspend fun searchUserInfos(@Query("userId") userIds: List<String>?): ApiResponse<List<UserInfo>>

    @GET("/user/avatars")
    suspend fun getUserAvatars(@Query("userId") userIds: List<String>?): ApiResponse<List<UserAvatar>>
}
