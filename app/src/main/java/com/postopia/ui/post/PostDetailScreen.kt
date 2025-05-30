package com.postopia.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.domain.mapper.PostMapper.toUiModel
import com.postopia.domain.repository.PostRepository
import com.postopia.ui.model.PostDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostDetailUiState(
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,
    val postDetail: PostDetailUiModel = PostDetailUiModel.default(),
)

sealed class PostDetailEvent {
    object SnackbarMessageShown : PostDetailEvent()
    data class LoadPostDetail(val postId: Long, val spaceId: Long, val spaceNam: String) : PostDetailEvent()
}

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: PostDetailEvent) {
        when (event) {
            is PostDetailEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is PostDetailEvent.LoadPostDetail -> {
                loadPostDetail(event.postId, event.spaceId, event.spaceNam)
            }
        }
    }

    fun loadPostDetail(postId: Long, spaceId: Long, spaceName: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            postRepository.getPostByID(postId).collect { result->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update { it.copy(postDetail = result.data.toUiModel(spaceId, spaceName), isLoading = false) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }
}