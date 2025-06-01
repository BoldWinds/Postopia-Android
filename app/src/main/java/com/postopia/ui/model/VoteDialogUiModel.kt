package com.postopia.ui.model

import com.postopia.data.model.OpinionStatus
import com.postopia.data.model.VoteType

data class VoteDialogUiModel(
    val voteID : Long,
    val voteType: VoteType,
    val initiatorID : Long,
    val initiatorName : String,
    val initiatorAvatar : String,
    val relatedUserID : Long? = null,
    val relatedUserName : String? = null,
    val relatedUserAvatar : String? = null,
    val reason : String? = null,
    val startAt : String,
    val endAt : String,
    val positiveCount : Long,
    val negativeCount : Long,
    val opinion: OpinionStatus,
) {
    companion object {
        fun default() : VoteDialogUiModel = VoteDialogUiModel(
            voteID = -1,
            voteType = VoteType.UNARCHIVE_POST,
            initiatorID = -1,
            initiatorName = "Default User",
            initiatorAvatar = "",
            relatedUserID = null,
            relatedUserName = null,
            relatedUserAvatar = null,
            reason = null,
            startAt = "",
            endAt = "",
            positiveCount = 0L,
            negativeCount = 0L,
            opinion = OpinionStatus.NIL
        )
    }
}