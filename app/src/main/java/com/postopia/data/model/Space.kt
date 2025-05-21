package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Space(
    val id: Int,
    val name: String,
    val avatar: String,
    val description: String,
    val createdAt: String,
    val postCount: Int,
    val memberCount: Int
)

@JsonClass(generateAdapter = true)
data class SpaceAvatar(
    val name: String,
    val avatar: String
)

@JsonClass(generateAdapter = true)
data class SpacePart(
    val name: String,
    val avatar: String,
    val postCount: Int,
    val memberCount: Int,
)
