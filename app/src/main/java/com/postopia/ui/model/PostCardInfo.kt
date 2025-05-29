package com.postopia.ui.model

import com.postopia.data.model.OpinionStatus

data class PostCardInfo(
    val postID : Long,
    val subject: String,
    val content: String,
    val userAvatar: String,
    val userNickname: String,
    val positiveCount: String,
    val negativeCount: String,
    val commentCount: String,
    val createdAt: String,  // need format
    val opinionStatus: OpinionStatus,
)