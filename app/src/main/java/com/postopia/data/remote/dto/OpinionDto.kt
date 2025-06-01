package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostOpinionRequest(
    val postId: Long,
    val spaceId: Long,
    val userId: Long,
    val isPositive: Boolean,
)

@JsonClass(generateAdapter = true)
data class CancelOpinionRequest(
    val id: Long,
    val isPositive: Boolean,
)

@JsonClass(generateAdapter = true)
data class PostCommentRequest(
    val commentId: Long,
    val spaceId: Long,
    val userId: Long,
    val isPositive: Boolean,
)

@JsonClass(generateAdapter = true)
data class VoteOpinionRequest(
    val id: Long,
    val isPositive: Boolean,
)

