package com.postopia.domain.mapper

import com.postopia.data.model.SpaceVoteInfo
import com.postopia.data.model.VoteInfo
import com.postopia.ui.model.VoteDialogUiModel

object VoteMapper {
    fun VoteInfo.toUiModel(): VoteDialogUiModel {
        return VoteDialogUiModel(
            voteID = this.vote.id,
            voteType = this.vote.voteType,
            initiatorID = this.initiator.userId,
            initiatorName = this.initiator.username,
            initiatorAvatar = this.initiator.avatar,
            startAt = this.vote.startAt,
            endAt = this.vote.endAt,
            positiveCount = this.vote.positiveCount,
            negativeCount = this.vote.negativeCount,
            opinion = this.opinion.opinionStatus,
            reason = null,
            relatedUserID = null,
            relatedUserName = null,
            relatedUserAvatar = null
        )
    }

    fun SpaceVoteInfo.toUiModel(): VoteDialogUiModel {
        return VoteDialogUiModel(
            voteID = this.vote.id,
            voteType = this.vote.voteType,
            initiatorID = this.initiator.userId,
            initiatorName = this.initiator.username,
            initiatorAvatar = this.initiator.avatar,
            relatedUserID = this.relatedUser?.userId,
            relatedUserName = this.relatedUser?.username,
            relatedUserAvatar = this.relatedUser?.avatar,
            reason = this.vote.second, // reason存储在second字段中
            startAt = this.vote.startAt,
            endAt = this.vote.endAt,
            positiveCount = this.vote.positiveCount,
            negativeCount = this.vote.negativeCount,
            opinion = this.opinion.opinionStatus
        )
    }
}

