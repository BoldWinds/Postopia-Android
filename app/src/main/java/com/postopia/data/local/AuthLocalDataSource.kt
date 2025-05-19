package com.postopia.data.local

import com.postopia.data.model.User
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {

    suspend fun saveUser(user: User)

    suspend fun updateUsername(username: String)

    suspend fun updateNickname(nickname: String)

    suspend fun updateEmail(email: String)

    suspend fun updateIntroduction(introduction: String)

    suspend fun saveAccessToken(token: String)

    suspend fun saveRefreshToken(token: String)

    fun getUser(): Flow<User?>

    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    suspend fun clearUserData()

    fun isLoggedIn(): Flow<Boolean>
}