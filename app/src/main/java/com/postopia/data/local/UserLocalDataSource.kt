package com.postopia.data.local

import com.postopia.data.model.UserDetail

interface UserLocalDataSource {

    suspend fun getUserDetail(userId: String): UserDetail?

    suspend fun cacheUserDetail(userId: String, userDetail: UserDetail)

}