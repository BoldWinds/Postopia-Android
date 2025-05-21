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



