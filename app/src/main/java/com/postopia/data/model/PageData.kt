package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageData<T>(
    val currentPage: Long,
    val totalPage: Long,
    val data: List<T>? = null
){
    fun requireData(): List<T> {
        if (data == null) {
            throw IllegalStateException("No data")
        }
        return data
    }
}