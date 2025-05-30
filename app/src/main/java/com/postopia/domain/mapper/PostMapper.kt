package com.postopia.domain.mapper

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.PostInfo
import com.postopia.data.model.UserPostInfo
import com.postopia.ui.model.PostCardInfo
import com.postopia.ui.model.PostDetailUiModel

object PostMapper {

    fun PostInfo.toPostCardInfo(spaceID: Long, spaceName: String): PostCardInfo {
        return PostCardInfo(
            postID = this.post.id,
            spaceID = spaceID,
            spaceName = spaceName,
            subject = this.post.subject,
            content = this.post.content,
            userAvatar = this.user.avatar,
            userNickname = this.user.nickname,
            positiveCount = this.post.positiveCount.toString(),
            negativeCount = this.post.negativeCount.toString(),
            commentCount = this.post.commentCount.toString(),
            createdAt = this.post.createdAt,
            opinionStatus = this.opinion.opinionStatus,
        )
    }

    fun PostInfo.toUiModel(spaceId: Long, spaceName: String): PostDetailUiModel {
           return PostDetailUiModel(
                postID = this.post.id,
                userID = this.user.userId,
                nickname = this.user.nickname,
                avatar = this.user.avatar,
                spaceID = spaceId,
                spaceName = spaceName,
                subject = this.post.subject,
                content = this.post.content,
                positiveCount = this.post.positiveCount,
                negativeCount = this.post.negativeCount,
                opinionStatus = this.opinion.opinionStatus,
                commentCount = this.post.commentCount,
                isArchived = this.post.isArchived,
                createdAt = this.post.createdAt
           )
    }

    fun FeedPostInfo.toPostCardInfo(): PostCardInfo {
        return PostCardInfo(
            postID = this.post.id,
            spaceID = this.post.spaceId,
            spaceName = this.post.spaceName,
            subject = this.post.subject,
            content = this.post.content,
            userAvatar = this.user.avatar,
            userNickname = this.user.nickname,
            positiveCount = this.post.positiveCount.toString(),
            negativeCount = this.post.negativeCount.toString(),
            commentCount = this.post.commentCount.toString(),
            createdAt = this.post.createdAt,
            opinionStatus = this.opinion.opinionStatus
        )
    }

    fun UserPostInfo.toPostCardInfo(userAvatar: String, userNickname: String): PostCardInfo {
        return PostCardInfo(
            postID = this.post.id,
            spaceID = this.post.spaceId,
            spaceName = this.post.spaceName,
            subject = this.post.subject,
            content = this.post.content,
            userAvatar = userAvatar,
            userNickname = userNickname,
            positiveCount = this.post.positiveCount.toString(),
            negativeCount = this.post.negativeCount.toString(),
            commentCount = this.post.commentCount.toString(),
            createdAt = this.post.createdAt,
            opinionStatus = this.opinion.opinionStatus
        )
    }

}

