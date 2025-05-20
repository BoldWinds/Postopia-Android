package com.postopia.data.local.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.postopia.data.local.AuthLocalDataSource
import com.postopia.data.model.Credential
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credential")

@Singleton
class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthLocalDataSource{

    private object PreferencesKeys {
        val USERID = intPreferencesKey("userId")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveCredential(credential: Credential) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERID] = credential.userId
            preferences[PreferencesKeys.ACCESS_TOKEN] = credential.accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = credential.refreshToken
        }
    }

    override fun getAccessToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACCESS_TOKEN]
    }

    override fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.REFRESH_TOKEN]
    }

    override fun getUserId(): Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USERID]
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

    override suspend fun saveUserId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERID] = id
        }
    }
}