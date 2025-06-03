package com.postopia.ui.model

data class CommentCardUiModel(
    val commentId: Long,
    val spaceId: Long,
    val spaceName: String,
    val postId: Long,
    val postSubject: String,
    val authorId: Long,
    val authorName: String,
    val authorAvatar: String,
    val createdAt: String,
    val content: String,
)