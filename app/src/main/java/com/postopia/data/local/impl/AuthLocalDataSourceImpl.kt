package com.postopia.data.local.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.postopia.data.local.AuthLocalDataSource
import com.postopia.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 为Context类扩展dataStore属性
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthLocalDataSource {

    // 定义所有偏好设置键
    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val NICKNAME = stringPreferencesKey("nickname")
        val EMAIL = stringPreferencesKey("email")
        val INTRODUCTION = stringPreferencesKey("introduction")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = user.username
            preferences[PreferencesKeys.NICKNAME] = user.nickname
            preferences[PreferencesKeys.EMAIL] = user.email
            preferences[PreferencesKeys.INTRODUCTION] = user.introduction
            preferences[PreferencesKeys.ACCESS_TOKEN] = user.accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = user.refreshToken
        }
    }

    override suspend fun updateUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
        }
    }

    override suspend fun updateNickname(nickname: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NICKNAME] = nickname
        }
    }

    override suspend fun updateEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.EMAIL] = email
        }
    }

    override suspend fun updateIntroduction(introduction: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.INTRODUCTION] = introduction
        }
    }

    override suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = token
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN] = token
        }
    }

    override fun getUser(): Flow<User?> = context.dataStore.data.map { preferences ->
        val username = preferences[PreferencesKeys.USERNAME] ?: return@map null

        User(
            username = username,
            nickname = preferences[PreferencesKeys.NICKNAME] ?: "",
            email = preferences[PreferencesKeys.EMAIL] ?: "",
            introduction = preferences[PreferencesKeys.INTRODUCTION] ?: "",
            accessToken = preferences[PreferencesKeys.ACCESS_TOKEN] ?: "",
            refreshToken = preferences[PreferencesKeys.REFRESH_TOKEN] ?: ""
        )
    }

    override fun getAccessToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACCESS_TOKEN]
    }

    override fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REFRESH_TOKEN]
    }

    override suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        // 通过检查访问令牌是否存在来判断用户是否已登录
        preferences[PreferencesKeys.ACCESS_TOKEN]?.isNotEmpty() == true
    }
}

