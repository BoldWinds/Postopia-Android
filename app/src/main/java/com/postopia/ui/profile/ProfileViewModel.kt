package com.postopia.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.domain.mapper.CommentMapper.toUiModel
import com.postopia.domain.mapper.PostMapper.toPostCardInfo
import com.postopia.domain.mapper.ProfileMapper.toUiModel
import com.postopia.domain.repository.CommentRepository
import com.postopia.domain.repository.PostRepository
import com.postopia.domain.repository.UserRepository
import com.postopia.ui.model.CommentCardUiModel
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
    val selectedTab: Int = 0,
    val userPosts : List<PostCardInfo> = emptyList(),
    val isLoadingPosts : Boolean = false,
    val hasMorePosts : Boolean = false,
    val postPage : Int = 0,
    val userComments: List<CommentCardUiModel> = emptyList(),
    val isLoadingComments: Boolean = false,
    val hasMoreComments: Boolean = false,
    val commentPage: Int = 0,
)

sealed class ProfileEvent {
    object SnackbarMessageShown : ProfileEvent()
    object LoadMorePosts : ProfileEvent()
    object LoadMoreComments : ProfileEvent()
    data class ChangeTab(val tabIndex: Int) : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is ProfileEvent.LoadMorePosts -> {
                loadUserPosts(_uiState.value.userDetail.username, _uiState.value.userDetail.avatar)
            }
            is ProfileEvent.ChangeTab -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
                if(event.tabIndex == 1 && _uiState.value.userComments.isEmpty()) loadUserComments()
            }
            is ProfileEvent.LoadMoreComments -> {
                loadUserComments()
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        val userDetail = result.data.toUiModel()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userDetail = userDetail,
                            )
                        }
                        loadUserPosts(userDetail.nickname, userDetail.avatar)
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

    fun loadUserPosts(nickname: String, avatar: String) {
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
                                hasMorePosts = result.data.size == 20,
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

    fun loadUserComments(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingComments = true) }
            commentRepository.getUserComments(_uiState.value.commentPage).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoadingComments = false,
                                userComments = it.userComments + result.data.map { it.toUiModel() },
                                hasMoreComments = result.data.size == 20,
                                commentPage = it.commentPage + 1
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
