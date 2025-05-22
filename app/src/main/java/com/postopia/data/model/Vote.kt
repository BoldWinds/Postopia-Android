package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VotePart (
    val endAt: String,
    val id: Long,
    val initiator: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val relatedEntity: Long,
    val startAt: String,
    val voteType: String
)

@JsonClass(generateAdapter = true)
data class SpaceVotePart (
    val endAt: String,
    /**
     * username/description
     */
    val field1: String,
    /**
     * reason/avatar
     */
    val field2: String,

    val id: Long,
    val initiator: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val relatedEntity: Long,
    val relatedUser: Long,
    val startAt: String,
    val voteType: DetailVoteType
)

@JsonClass(generateAdapter = true)
data class VoteInfo (
    val initiator: UserInfo,

    /**
     * 组合各部分的辅助字段
     */
    val mergeID: Long,

    val opinion: OpinionInfo,
    val vote: VotePart
)

@JsonClass(generateAdapter = true)
data class SpaceVoteInfo (
    val initiator: UserInfo,


    val mergeID: Long,

    val opinion: OpinionInfo,

    /**
     * 驱逐或禁言对象
     */
    val relatedUser: UserInfo,

    val vote: SpaceVotePart
)

enum class DetailVoteType {
    PIN_COMMENT,
    UNPIN_COMMENT,
    DELETE_COMMENT,
    ARCHIVE_POST,
    UNARCHIVE_POST,
    DELETE_POST,
    UPDATE_SPACE,
    EXPEL_USER,
    MUTE_USER
}
