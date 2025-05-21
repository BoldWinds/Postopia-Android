package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.Credential
import com.postopia.data.remote.dto.LoginRequest
import com.postopia.data.remote.dto.RefreshTokenRequest
import com.postopia.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRemoteDataSource {
    @POST("user/auth/signup")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<Unit>

    @POST("user/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<Credential>

    @POST("user/auth/refresh")
    suspend fun refresh(@Body request : RefreshTokenRequest) : ApiResponse<String>
}

