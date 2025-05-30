package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostOpinionRequest(
    val postId: Long,
    val spaceId: Long,
    val userId: Long,
    val isPositive: Boolean,
)