package com.postopia.data.local.impl

import android.content.Context
import com.postopia.data.local.UserLocalDataSource
import com.postopia.data.local.proto.userDataStore
import com.postopia.data.model.UserDetail
import com.postopia.proto.UserProto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSourceImpl  @Inject constructor(
    @ApplicationContext private val context: Context
) : UserLocalDataSource {

    override suspend fun getUserDetail(userId: String): UserDetail? {
        return context.userDataStore.data.map { userProto ->
            if (userProto == UserProto.getDefaultInstance()) {
                null
            } else {
                UserDetail(
                    userId = userProto.userID,
                    avatar = userProto.avatar,
                    commentCount = userProto.commentCount,
                    createdAt = userProto.createdAt,
                    credit = userProto.credit,
                    email = userProto.email.takeIf { it.isNotEmpty() },
                    introduction = userProto.introduction.takeIf { it.isNotEmpty() },
                    nickname = userProto.nickname,
                    postCount = userProto.postCount,
                    showEmail = userProto.showEmail,
                    username = userProto.username
                )
            }
        }.first()
    }

    override suspend fun cacheUserDetail(userId: String, userDetail: UserDetail) {
        context.userDataStore.updateData { currentUserProto ->
            currentUserProto.toBuilder()
                .setUserID(userDetail.userId)
                .setAvatar(userDetail.avatar)
                .setCommentCount(userDetail.commentCount)
                .setCreatedAt(userDetail.createdAt)
                .setCredit(userDetail.credit)
                .setEmail(userDetail.email?.toString() ?: "")
                .setIntroduction(userDetail.introduction?.toString() ?: "")
                .setNickname(userDetail.nickname)
                .setPostCount(userDetail.postCount)
                .setShowEmail(userDetail.showEmail)
                .setUsername(userDetail.username)
                .build()
        }
    }
}
