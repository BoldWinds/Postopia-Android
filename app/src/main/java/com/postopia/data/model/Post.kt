package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostPart (
    val id: Long,
    val userID: Long,
    val commentCount: Long,
    val content: String,
    val createdAt: String,
    val isArchived: Boolean,
    val negativeCount: Long,
    val positiveCount: Long,
    val subject: String,
)

@JsonClass(generateAdapter = true)
data class FeedPostPart (
    val commentCount: Long,
    val content: String,
    val createdAt: String,
    val id: Long,
    val isArchived: Boolean,
    val negativeCount: Long,
    val positiveCount: Long,
    val spaceID: Long,
    val spaceName: String,
    val subject: String,
    val userID: Long
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