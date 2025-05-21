package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Space(
    val id: Long,
    val name: String,
    val avatar: String,
    val description: String,
    val createdAt: String,
    val postCount: Long,
    val memberCount: Long,
)

@JsonClass(generateAdapter = true)
data class SpaceAvatar(
    val name: String,
    val avatar: String,
)

@JsonClass(generateAdapter = true)
data class SearchSpaceInfo(
    val id: Long,
    val avatar: String,
    val createdAt: String,
    val memberCount: Long,
    val postCount: Long
)
