package com.postopia.ui.model

import com.postopia.data.model.OpinionStatus

data class PostDetailUiModel(
    val postID : Long,
    val userID : Long,
    val nickname : String,
    val avatar : String,
    val spaceID : Long,
    val spaceName : String,
    val subject : String,
    val content : String,
    val positiveCount : Long,
    val negativeCount : Long,
    val opinionStatus : OpinionStatus,
    val commentCount : Long,
    val isArchived : Boolean,
    val createdAt : String,
) {
    companion object {
        fun default(): PostDetailUiModel = PostDetailUiModel(
            postID = -1,
            userID = -1,
            nickname = "Default User",
            avatar = "",
            spaceID = -1,
            spaceName = "Default Space",
            subject = "Default Subject",
            content = "Default Content",
            positiveCount = -1,
            negativeCount = -1,
            opinionStatus = OpinionStatus.NIL,
            commentCount = -1,
            isArchived = false,
            createdAt = "",
        )
    }
}