package com.postopia.data.model

data class User(
    val userId : Int,
    val username: String,
    val nickname: String = "",
    val avatar: String = "",
    val postCount : Int = 0 ,
    val commentCount: Int = 0,
    val credit : Int = 0,
    val email: String = "",
    val introduction : String = ""
)
