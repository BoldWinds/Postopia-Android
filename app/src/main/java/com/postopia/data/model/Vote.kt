package com.postopia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VotePart (
    val id: Long,
    val voteType: VoteType,
    val initiator: Long,
    val relatedEntity: Long,    // spaceID
    val negativeCount: Long,
    val positiveCount: Long,
    val startAt: String,
    val endAt: String,

)

@JsonClass(generateAdapter = true)
data class SpaceVotePart (
    val id: Long,
    val voteType: VoteType,
    val initiator: Long,
    val negativeCount: Long,
    val positiveCount: Long,
    val relatedEntity: Long,    // spaceID
    val relatedUser: Long? = null,
    val startAt: String,
    val endAt: String,
    /**
     * username/description
     */
    val first: String,
    /**
     * reason/avatar
     */
    val second: String,
)

@JsonClass(generateAdapter = true)
data class VoteInfo (
    /**
     * 组合各部分的辅助字段
     */
    val mergeId: Long,
    val initiator: UserInfo,
    val opinion: OpinionInfo,
    val vote: VotePart,
)

@JsonClass(generateAdapter = true)
data class SpaceVoteInfo (
    val mergeId: Long,
    /**
     * 驱逐或禁言对象
     */
    val relatedUser: UserInfo ? = null,
    val opinion: OpinionInfo,
    val initiator: UserInfo,
    val vote: SpaceVotePart,
)

enum class VoteType {
    PIN_COMMENT {
        override fun toString(): String = "置顶评论"
    },
    UNPIN_COMMENT {
        override fun toString(): String = "取消置顶评论"
    },
    DELETE_COMMENT {
        override fun toString(): String = "删除评论"
    },
    ARCHIVE_POST {
        override fun toString(): String = "归档帖子"
    },
    UNARCHIVE_POST {
        override fun toString(): String = "取消归档帖子"
    },
    DELETE_POST {
        override fun toString(): String = "删除帖子"
    },
    UPDATE_SPACE {
        override fun toString(): String = "更新空间"
    },
    EXPEL_USER {
        override fun toString(): String = "驱逐用户"
    },
    MUTE_USER {
        override fun toString(): String = "禁言用户"
    }
}
