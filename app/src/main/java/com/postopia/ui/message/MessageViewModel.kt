package com.postopia.ui.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.domain.mapper.MessageMapper.toUiModel
import com.postopia.domain.repository.MessageRepository
import com.postopia.ui.model.MessageCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageUiState(
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,
    val messages: List<MessageCardUiModel> = emptyList(),
    val isSelectionMode : Boolean = false,
    val selectedMessages : List<Long> = emptyList(),
    val isLoadingMessages : Boolean = false,
    val hasMoreMessages : Boolean = false,
    val messagePage : Int = 0,
)

sealed class MessageEvent {
    object SnackbarMessageShown : MessageEvent()
    object LoadMessages : MessageEvent()
    object DeleteReadMessages : MessageEvent()
    object ReadAllMessages : MessageEvent()
    object SelectAllMessages : MessageEvent()
    object DeselectAllMessages : MessageEvent()
    data class EnterSelectionMode(val messageId : Long) : MessageEvent()
    object ExitSelectionMode : MessageEvent()
    data class SelectMessage(val messageId: Long) : MessageEvent()
    data class DeselectMessage(val messageId: Long) : MessageEvent()
    data class DeleteMessage(val messageIds: List<Long>) : MessageEvent()
    data class ReadMessage(val messageIds: List<Long>) : MessageEvent()
}

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true) }
        loadMessages()
    }

    fun handleEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is MessageEvent.LoadMessages -> {
                loadMessages()
            }
            is MessageEvent.DeleteMessage -> {
                deleteMessage(event.messageIds)
            }
            is MessageEvent.ReadMessage -> {
                readMessage(event.messageIds)
            }
            is MessageEvent.ReadAllMessages -> {
                readAllMessages()
            }
            is MessageEvent.DeleteReadMessages -> {
                deleteReadMessages()
            }
            is MessageEvent.EnterSelectionMode -> {
                _uiState.update { it.copy(isSelectionMode = true, selectedMessages = listOf(event.messageId)) }
            }
            is MessageEvent.ExitSelectionMode -> {
                _uiState.update { it.copy(isSelectionMode = false, selectedMessages = emptyList()) }
            }
            is MessageEvent.SelectAllMessages -> {
                _uiState.update { state ->
                    if (state.isSelectionMode) {
                        state.copy(selectedMessages = state.messages.map { it.id })
                    } else {
                        state.copy(isSelectionMode = true, selectedMessages = state.messages.map { it.id })
                    }
                }
            }
            is MessageEvent.DeselectAllMessages -> {
                _uiState.update { state ->
                    state.copy(selectedMessages = emptyList())
                }
            }
            is MessageEvent.SelectMessage -> {
                _uiState.update { state ->
                    state.copy(selectedMessages = state.selectedMessages + event.messageId)
                }
            }
            is MessageEvent.DeselectMessage -> {
                _uiState.update { state ->
                    state.copy(selectedMessages = state.selectedMessages - event.messageId)
                }
            }
        }
    }

    fun loadMessages() {
        val page = _uiState.value.messagePage
        viewModelScope.launch {
            messageRepository.getMessages(page).collect { result->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoadingMessages = true) }
                    }
                    is Result.Success -> {
                        val newMessages = result.data.map { it.toUiModel() }
                        _uiState.update { state ->
                            state.copy(
                                messages = state.messages + newMessages,
                                isLoadingMessages = false,
                                isLoading = false,
                                hasMoreMessages = newMessages.size >= 20,
                                messagePage = page + 1
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoadingMessages = false, isLoading = false, snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }

    fun deleteMessage(messageIds: List<Long>) {
        viewModelScope.launch {
            messageRepository.deleteMessages(messageIds).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                messages = state.messages.filterNot { messageIds.contains(it.id) },
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }

    fun readMessage(messageIds: List<Long>) {
        viewModelScope.launch {
            messageRepository.readMessages(messageIds).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update { state ->
                            val updatedMessages = state.messages.map { message ->
                                if (messageIds.contains(message.id)) {
                                    message.copy(isRead = true)
                                } else {
                                    message
                                }
                            }
                            state.copy(messages = updatedMessages)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }

    fun readAllMessages() {
        viewModelScope.launch {
            messageRepository.readAllMessages().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                messages = state.messages.map { it.copy(isRead = true) },
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }

    fun deleteReadMessages() {
        viewModelScope.launch {
            messageRepository.deleteReadMessages().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                messages = state.messages.filterNot { it.isRead },
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }
}