package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpinionInfo (
    val mergeID: Long,
    val opinionStatus: OpinionStatus,
    val updatedAt: String
)

enum class OpinionStatus {
    Negative,
    Nil,
    Positive
}