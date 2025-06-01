package com.postopia.data.model

data class Message(
    val id: Long,
    val content: String,
    val isRead: Boolean,
    val createdAt: String,
)