package com.postopia.data.local

import com.postopia.data.model.Credential
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {

    suspend fun saveCredential(credential: Credential)

    suspend fun saveAccessToken(token: String)

    suspend fun saveRefreshToken(token: String)

    suspend fun saveUserId(id: Long)

    fun getAccessToken(): Flow<String?>

    fun getRefreshToken(): Flow<String?>

    fun getUserId() : Flow<Long?>
}