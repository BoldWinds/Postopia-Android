package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentPart (
    val id: Long,
    val userId: Long,
    val parentId: Long? = null,
    val content: String,
    val createdAt: String,
    val isPined: Boolean,
    val negativeCount: Long,
    val positiveCount: Long,
)

@JsonClass(generateAdapter = true)
data class SpaceCommentPart (
    val id: Long,
    val postId: Long,
    val spaceId: Long,
    val userId: Long,
    val parentId: Long? = null,
    val content: String,
    val negativeCount: Long,
    val positiveCount: Long,
    val createdAt: String,
)

@JsonClass(generateAdapter = true)
data class SearchCommentPart (
    val createdAt: String,
    val id: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val postId: Long,
    val userId: Long
)

@JsonClass(generateAdapter = true)
data class CommentInfo (
    val comment: CommentPart,
    val opinion: OpinionInfo,
    val user: UserInfo,
    val vote: VoteInfo? = null,
)

@JsonClass(generateAdapter = true)
data class UserCommentInfo (
    val comment: SpaceCommentPart,
    val opinion: OpinionInfo
)


@JsonClass(generateAdapter = true)
data class OpinionCommentInfo (
    val comment: SpaceCommentPart,
    val opinion: OpinionInfo,
    val user: UserInfo
)

@JsonClass(generateAdapter = true)
data class SearchCommentInfo (
    val comment: SearchCommentPart,
    val opinion: OpinionInfo,
    val post: CommentPostInfo,
    val user: UserInfo
)

@JsonClass(generateAdapter = true)
data class RecursiveCommentInfo(
    val comment : CommentInfo,
    val children : List<RecursiveCommentInfo>,
)

data class GeneralCommentInfo(
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
