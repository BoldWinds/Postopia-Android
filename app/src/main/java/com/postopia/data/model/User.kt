package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val userId : Int,
    val username: String,
    val nickname: String = "",
    val avatar: String = "",
    val introduction: String,
    val postCount : Int = 0 ,
    val commentCount: Int = 0,
    val credit : Int = 0,
)

@JsonClass(generateAdapter = true)
data class UserAvatar (
    val avatar: String,
    val userID: Long
)

@JsonClass(generateAdapter = true)
data class UserDetail (
    val avatar: String,
    val commentCount: Long,
    val createdAt: String,
    val credit: Long,
    val email: Any? = null,
    val introduction: Any? = null,
    val nickname: String,
    val postCount: Long,
    val showEmail: Boolean,
    val userID: Long,
    val username: String
)


