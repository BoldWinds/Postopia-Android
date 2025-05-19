package com.postopia.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String? = null,
    @Json(name = "data") val data: T? = null
) {
    fun isSuccessful(): Boolean = success

    fun getErrorMessageOrDefault(): String {
        return message ?: "Unknown error occurred"
    }

    fun requireData(): T {
        if (!success || data == null) {
            throw IllegalStateException(getErrorMessageOrDefault())
        }
        return data
    }
}