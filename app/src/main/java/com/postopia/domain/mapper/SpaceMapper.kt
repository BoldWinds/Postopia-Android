package com.postopia.domain.mapper

import com.postopia.data.model.SpaceInfo
import com.postopia.ui.model.SpaceDetailUiModel

object SpaceMapper {

    fun SpaceInfo.toUiModel(): SpaceDetailUiModel {
        return SpaceDetailUiModel(
            spaceID = this.space.id,
            name = this.space.name,
            avatar = this.space.avatar,
            description = this.space.description,
            memberCount = this.space.memberCount,
            postCount = this.space.postCount,
            isMember = this.isMember,
            createdAt = this.space.createdAt
        )
    }
}