package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VotePart (
    val id: Long,
    val voteType: String,
    val initiator: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val relatedEntity: Long,
    val startAt: String,
    val endAt: String,

)

@JsonClass(generateAdapter = true)
data class SpaceVotePart (
    val id: Long,
    val voteType: DetailVoteType,
    val initiator: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val relatedEntity: Long,
    val relatedUser: Long,
    val startAt: String,
    val endAt: String,
    /**
     * username/description
     */
    val field1: String,
    /**
     * reason/avatar
     */
    val field2: String,
)

@JsonClass(generateAdapter = true)
data class VoteInfo (
    /**
     * 组合各部分的辅助字段
     */
    val mergeID: Long,
    val initiator: UserInfo,
    val opinion: OpinionInfo,
    val vote: VotePart
)

@JsonClass(generateAdapter = true)
data class SpaceVoteInfo (
    val mergeID: Long,
    /**
     * 驱逐或禁言对象
     */
    val relatedUser: UserInfo,
    val opinion: OpinionInfo,
    val initiator: UserInfo,
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
