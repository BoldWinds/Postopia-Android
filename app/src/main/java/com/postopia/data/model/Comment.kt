package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentPart (
    val content: String,
    val createdAt: String,
    val id: Long,
    val isPined: Boolean,
    val negativeCount: Long,
    val parentID: Long,
    val positiveCount: Long,
    val userID: Long
)

@JsonClass(generateAdapter = true)
data class SpaceCommentPart (
    val content: String,
    val id: Long,
    val negativeCount: Long,
    val parentID: Long,
    val positiveCount: Long,
    val postID: Long,
    val spaceID: Long,
    val userID: Long
)

@JsonClass(generateAdapter = true)
data class SearchCommentPart (
    val createdAt: String,
    val id: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val postID: Long,
    val userID: Long
)

@JsonClass(generateAdapter = true)
data class CommentInfo (
    val comment: CommentPart,
    val opinion: OpinionInfo,
    val user: UserInfo,
    val vote: VoteInfo
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
