package com.postopia.data.remote

import com.postopia.data.local.AuthLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : Interceptor {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (shouldSkipAuth(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        val tokenValue = runBlocking { authLocalDataSource.getAccessToken().firstOrNull() }

        if (tokenValue.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        // 添加令牌到请求
        val newRequest = originalRequest.newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer $tokenValue")
            .build()

        return chain.proceed(newRequest)
    }

    private fun shouldSkipAuth(request: Request): Boolean {
        val url = request.url.toString()
        return url.contains("/user/auth/")
    }
}

