package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateCommentRequest(
    val postId: Long,
    val content: String,
    val spaceId: Long,
    val userId: Long,
    val parentId: Long? = null,
)
