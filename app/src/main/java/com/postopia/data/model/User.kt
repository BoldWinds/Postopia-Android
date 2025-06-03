package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfo(
    val userId : Long,
    val username: String,
    val nickname: String = "",
    val avatar: String = "",
    val introduction: String? = null,
    val postCount : Long = 0 ,
    val commentCount: Long = 0,
    val credit : Long = 0,
    val createdAt: String,
)

@JsonClass(generateAdapter = true)
data class UserAvatar (
    val avatar: String,
    val userId: Long
)

@JsonClass(generateAdapter = true)
data class UserDetail (
    val userId: Long,
    val avatar: String,
    val commentCount: Long,
    val createdAt: String,
    val credit: Long,
    val email: Any? = null,
    val introduction: Any? = null,
    val nickname: String,
    val postCount: Long,
    val showEmail: Boolean,
    val username: String
)

