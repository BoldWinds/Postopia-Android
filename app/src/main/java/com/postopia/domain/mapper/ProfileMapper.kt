package com.postopia.domain.mapper

import com.postopia.data.model.UserDetail
import com.postopia.data.model.UserInfo
import com.postopia.ui.model.ProfileUiModel

object ProfileMapper {
    fun UserDetail.toUiModel(): ProfileUiModel {
        return ProfileUiModel(
            id = this.userId,
            username = this.username,
            nickname = this.nickname,
            avatar = this.avatar,
            createdAt = this.createdAt,
            postCount = this.postCount,
            commentCount = this.commentCount,
            credit = this.credit,
            email = if(this.email==null) "" else this.email.toString(),
            introduction = if(this.introduction==null) "" else this.introduction.toString(),
        )
    }

    fun UserInfo.toUiModel(): ProfileUiModel {
        return ProfileUiModel(
            id = this.userId,
            username = this.username,
            nickname = this.nickname,
            avatar = this.avatar,
            createdAt = this.createdAt,
            postCount = this.postCount,
            commentCount = this.commentCount,
            credit = this.credit,
            email = "",
            introduction = if(this.introduction==null) "" else this.introduction.toString(),
        )
    }
}