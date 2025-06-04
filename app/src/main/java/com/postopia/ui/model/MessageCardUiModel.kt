package com.postopia.ui.model

data class MessageCardUiModel(
    val id : Long,
    val content: String,
    val isRead : Boolean,
    val createdAt: String,
    val route: String,
)