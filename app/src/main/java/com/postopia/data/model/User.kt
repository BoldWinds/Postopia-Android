package com.postopia.data.model

data class User(
    val username: String,
    val nickname: String = "",
    val email: String = "",
    val introduction : String = "",
    val accessToken: String,
    val refreshToken: String
)
