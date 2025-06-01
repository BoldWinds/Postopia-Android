package com.postopia.domain.mapper

import com.postopia.data.model.Message
import com.postopia.ui.model.MessageCardUiModel

object MessageMapper {
    fun Message.toUiModel(): MessageCardUiModel {
        return MessageCardUiModel(
            id = id,
            content = content,
            createdAt = createdAt,
            isRead = isRead,
        )
    }
}