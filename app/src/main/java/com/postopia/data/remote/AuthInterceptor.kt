package com.postopia.data.remote

import com.postopia.data.local.AuthLocalDataSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

@Singleton
class AuthInterceptor @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : Interceptor {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 跳过登录和注册端点的认证
        if (shouldSkipAuth(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        // 由于OkHttp拦截器不支持挂起函数，我们需要使用runBlocking
        val accessToken = runBlocking { authLocalDataSource.getAccessToken() }

        // 如果没有令牌，继续原始请求
        /*if (accessToken.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }*/

        // 添加令牌到请求
        val newRequest = originalRequest.newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }

    private fun shouldSkipAuth(request: Request): Boolean {
        val url = request.url.toString()
        return url.contains("/user/auth/login") || url.contains("/user/auth/register")
    }
}