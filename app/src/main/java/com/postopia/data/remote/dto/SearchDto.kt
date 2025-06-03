package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpaceDoc(
    val id: String,
    val name: String,
    val description: String,
)

@JsonClass(generateAdapter = true)
data class UserDoc(
    val id: String,
    val name: String,
    val nickname: String,
)

@JsonClass(generateAdapter = true)
data class CommentDoc(
    val id: String,
    val content: String,
    val spaceId: Long,
    val postId: Long,
    val userId: Long,
)

@JsonClass(generateAdapter = true)
data class PostDoc(
    val id: String,
    val subject: String,
    val content: String,
    val spaceId: Long,
    val userId: Long,
)