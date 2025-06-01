package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteMessagesRequest(
    val ids: List<Long>
)

@JsonClass(generateAdapter = true)
data class ReadMessagesRequest(
    val ids: List<Long>
)