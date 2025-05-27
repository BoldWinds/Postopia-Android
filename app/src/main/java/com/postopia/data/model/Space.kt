package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpaceInfo(
    val space : SpacePart,
    val isMember : Boolean
)

@JsonClass(generateAdapter = true)
data class SpaceAvatar(
    val name: String,
    val avatar: String,
)

@JsonClass(generateAdapter = true)
data class SearchSpaceInfo(
    val space : SearchSpacePart,
    val isMember : Boolean
)

@JsonClass(generateAdapter = true)
data class SpacePart (
    val id: Long,
    val name: String,
    val avatar: String,
    val createdAt: String,
    val description: String,
    val memberCount: Long,
    val postCount: Long
)

@JsonClass(generateAdapter = true)
data class SearchSpacePart (
    val avatar: String,
    val createdAt: String,
    val id: Long,
    val memberCount: Long,
    val postCount: Long
)