package com.postopia.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateSpaceRequest(
    @Json(name = "info") val info: String,
)

@JsonClass(generateAdapter = true)
data class JoinSpaceRequest(
    @Json(name = "spaceId") val spaceId: String,
)

@JsonClass(generateAdapter = true)
data class LeaveSpaceRequest(
    @Json(name = "spaceId") val spaceId: String,
)

@JsonClass(generateAdapter = true)
data class SpaceInfoResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "avatar") val avatar: String,
    @Json(name = "description") val description: String,
    @Json(name = "createdAt") val createdAt: String,
    @Json(name = "postCount") val postCount: Int,
    @Json(name = "memberCount") val memberCount: Int,
)



