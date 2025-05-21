package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatePostRequest(
    val spaceId: Long,
    val spaceName: String,
    val subject: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class UpdatePostRequest(
    val id: String,
    val subject: String,
    val content: String,
    val spaceName: String
)