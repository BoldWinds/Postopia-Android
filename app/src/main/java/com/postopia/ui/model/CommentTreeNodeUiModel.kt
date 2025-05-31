package com.postopia.ui.model

import com.postopia.data.model.OpinionStatus

data class CommentTreeNodeUiModel(
    val id: Long,
    val userId: Long,
    val username: String,
    val nickname: String,
    val avatarUrl: String,
    val content: String,
    val timeAgo: String,
    val positiveCount: Long,
    val negativeCount: Long,
    val opinion: OpinionStatus,
    val isPinned: Boolean,
    val depth: Int = 0,
    val children: List<CommentTreeNodeUiModel>,
    // TODO vote
)
