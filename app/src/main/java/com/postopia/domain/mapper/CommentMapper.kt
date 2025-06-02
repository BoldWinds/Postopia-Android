package com.postopia.domain.mapper

import com.postopia.data.model.RecursiveCommentInfo
import com.postopia.ui.model.CommentTreeNodeUiModel

object CommentMapper {
    fun RecursiveCommentInfo.toUiModel(depth: Int = 0): CommentTreeNodeUiModel {
        return CommentTreeNodeUiModel(
            id = comment.comment.id,
            userId = comment.user.userId,
            username = comment.user.username,
            nickname = comment.user.nickname,
            avatarUrl = comment.user.avatar,
            content = comment.comment.content,
            createdAt = comment.comment.createdAt,
            positiveCount = comment.comment.positiveCount,
            negativeCount = comment.comment.negativeCount,
            opinion = comment.opinion.opinionStatus,
            isPinned = comment.comment.isPined,
            children = children.map { it.toUiModel(depth + 1) },
            depth = depth,
        )
    }
}