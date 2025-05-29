package com.postopia.domain.mapper

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.PostInfo
import com.postopia.data.model.UserPostInfo
import com.postopia.ui.model.PostCardInfo

object PostMapper {

    fun PostInfo.toPostCardInfo(): PostCardInfo {
        return PostCardInfo(
            postID = this.post.id,
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

    fun FeedPostInfo.toPostCardInfo(): PostCardInfo {
        return PostCardInfo(
            postID = this.post.id,
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

