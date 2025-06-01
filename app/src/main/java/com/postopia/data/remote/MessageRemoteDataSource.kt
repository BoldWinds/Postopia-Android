package com.postopia.data.remote

import com.postopia.data.model.ApiResponse
import com.postopia.data.model.Message
import com.postopia.data.model.PageData
import com.postopia.data.remote.dto.DeleteMessagesRequest
import com.postopia.data.remote.dto.ReadMessagesRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageRemoteDataSource {

    @GET("/message/user")
    suspend fun getUserMessages(@Query("page") page: Int = 0,@Query("size") size: String?= null): ApiResponse<PageData<Message>>

    @POST("/message/read")
    suspend fun readMessages(@Body request: ReadMessagesRequest): ApiResponse<Unit>

    @POST("/message/read-all")
    suspend fun readAllMessages(): ApiResponse<Unit>

    @POST("/message/delete")
    suspend fun deleteMessages(@Body request: DeleteMessagesRequest): ApiResponse<Unit>

    @POST("/message/delete-read")
    suspend fun deleteReadMessages(): ApiResponse<Unit>

    @GET("/message/unread")
    suspend fun getUnreadMessageCount(): ApiResponse<Long>
}