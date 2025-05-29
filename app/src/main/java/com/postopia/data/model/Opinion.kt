package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpinionInfo (
    val mergeId: Long,
    val opinionStatus: OpinionStatus,
    val updatedAt: String? = null
)

enum class OpinionStatus {
    NEGATIVE,
    NIL,
    POSITIVE
}
