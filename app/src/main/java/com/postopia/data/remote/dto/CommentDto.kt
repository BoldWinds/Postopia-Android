package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateCommentRequest(
    val commentId: Int,
    val postId: Int,
    val content: String,
    val spaceId: Int,
    val userId: Int,
    val parentId: Int? = null,
)
