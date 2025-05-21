package com.postopia.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateNicknameRequest(val nickname: String)

@JsonClass(generateAdapter = true)
data class UpdateIntroductionRequest(val introduction: String)

@JsonClass(generateAdapter = true)
data class UpdateEmailShowRequest(val show: Boolean)

@JsonClass(generateAdapter = true)
data class UpdateEmailRequest(val email: String)

@JsonClass(generateAdapter = true)
data class VerifyEmailRequest(val email: String, val authCode: String)
