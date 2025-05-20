package com.postopia.data.model

data class Credential(
    val userId : Int,
    val accessToken: String,
    val refreshToken: String
)