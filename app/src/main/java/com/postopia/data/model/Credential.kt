package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Credential(
    val userId : Long,
    val accessToken: String,
    val refreshToken: String
)