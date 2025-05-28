package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostPart (
    val id: Long,
    val userId: Long,
    val subject: String,
    val content: String,
    val isArchived: Boolean,
    val commentCount: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val createdAt: String,

)

@JsonClass(generateAdapter = true)
data class FeedPostPart (
    val id: Long,
    val userId: Long,
    val subject: String,
    val spaceId: Long,
    val spaceName: String,
    val content: String,
    val isArchived: Boolean,
    val commentCount: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val createdAt: String,
)

@JsonClass(generateAdapter = true)
data class PostInfo (
    val opinion: OpinionInfo,
    val post: PostPart,
    val user: UserInfo,
    val vote: VoteInfo
)

@JsonClass(generateAdapter = true)
data class UserPostInfo (
    val opinion: OpinionInfo,
    val post: FeedPostPart
)

@JsonClass(generateAdapter = true)
data class OpinionPostInfo (
    val opinion: OpinionInfo,
    val post: FeedPostPart,
    val user: UserInfo
)

@JsonClass(generateAdapter = true)
data class FeedPostInfo (
    val opinion: OpinionInfo,
    val post: FeedPostPart,
    val user: UserInfo,
    val vote: VoteInfo
)

@JsonClass(generateAdapter = true)
data class CommentPostInfo (
    val id: Long,
    val spaceName: String,
    val subject: String
)


fun PostPart.toFeedPostPart(spaceId: Long, spaceName: String): FeedPostPart {
    return FeedPostPart(
        id = this.id,
        userId = this.userId,
        subject = this.subject,
        spaceId = spaceId,
        spaceName = spaceName,
        content = this.content,
        isArchived = this.isArchived,
        commentCount = this.commentCount,
        negativeCount = this.negativeCount,
        positiveCount = this.positiveCount,
        createdAt = this.createdAt
    )
}

fun FeedPostPart.toPostPart(): PostPart {
    return PostPart(
        id = this.id,
        userId = this.userId,
        subject = this.subject,
        content = this.content,
        isArchived = this.isArchived,
        commentCount = this.commentCount,
        negativeCount = this.negativeCount,
        positiveCount = this.positiveCount,
        createdAt = this.createdAt
    )
}

fun PostInfo.toFeedPostInfo(spaceId: Long = 0, spaceName: String = ""): FeedPostInfo {
    return FeedPostInfo(
        opinion = this.opinion,
        post = this.post.toFeedPostPart(spaceId, spaceName),
        user = this.user,
        vote = this.vote
    )
}

fun FeedPostInfo.toPostInfo(): PostInfo {
    return PostInfo(
        opinion = this.opinion,
        post = this.post.toPostPart(),
        user = this.user,
        vote = this.vote
    )
}