package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VoteCommentRequest(
    val postId: Long,
    val userId: Long,
    val spaceId: Long,
    val commentId: Long,
    val commentContent: String,
)

@JsonClass(generateAdapter = true)
data class VotePostRequest(
    val postId: Long,
    val userId: Long,
    val spaceId: Long,
    val postSubject: String,
)

@JsonClass(generateAdapter = true)
data class VoteUserRequest(
    val spaceId: Long,
    val userId: Long,
    val username: String,
    val reason: String,
)