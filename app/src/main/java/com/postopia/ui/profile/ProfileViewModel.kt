package com.postopia.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.domain.mapper.PostMapper.toPostCardInfo
import com.postopia.domain.mapper.ProfileMapper.toUiModel
import com.postopia.domain.repository.PostRepository
import com.postopia.domain.repository.UserRepository
import com.postopia.ui.model.PostCardInfo
import com.postopia.ui.model.ProfileUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val userDetail: ProfileUiModel = ProfileUiModel.default(),
    val snackbarMessage: String? = null,
    val userPosts : List<PostCardInfo> = emptyList(),
    val isLoadingPosts : Boolean = false,
    val hasMorePosts : Boolean = true,
    val postPage : Int = 0,
)

sealed class ProfileEvent {
    object SnackbarMessageShown : ProfileEvent()
    object LoadMorePosts : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) } // Clear snackbar message
            }
            is ProfileEvent.LoadMorePosts -> {
                loadUserPosts()
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userDetail = result.data.toUiModel(),
                            )
                        }
                        loadUserPosts()
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                snackbarMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadUserPosts() {
        val nickname = _uiState.value.userDetail.nickname
        val avatar = _uiState.value.userDetail.avatar
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPosts = true) }
            postRepository.getUserPosts(_uiState.value.postPage).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingPosts = false,
                                userPosts = it.userPosts + result.data.map { it.toPostCardInfo(userNickname = nickname, userAvatar = avatar) },
                                hasMorePosts = result.data.isNotEmpty(),
                                postPage = it.postPage + 1
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingPosts = false,
                                snackbarMessage = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}
