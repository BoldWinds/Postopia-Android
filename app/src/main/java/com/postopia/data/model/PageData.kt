package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageData<T>(
    val currentPage: Int,
    val totalPage: Int,
    val data: T? = null
)