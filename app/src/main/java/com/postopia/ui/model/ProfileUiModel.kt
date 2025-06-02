package com.postopia.ui.model

data class ProfileUiModel (
    val id: Long,
    val username: String,
    val nickname: String,
    val avatar: String,
    val createdAt : String,
    val postCount: Long,
    val commentCount: Long,
    val credit: Long,
    val email: String,
    val introduction: String,
){
    companion object {
        fun default(): ProfileUiModel = ProfileUiModel(
            id = -1,
            username = "DefaultUser",
            nickname = "Default User",
            avatar = "",
            createdAt = "",
            postCount = -1,
            commentCount = -1,
            credit = -1,
            email = "",
            introduction = "",
        )
    }
}