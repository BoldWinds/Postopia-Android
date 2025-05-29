package com.postopia.ui.space

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.data.model.SpaceInfo
import com.postopia.data.repository.SpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SpaceUiState(
    val isLoading : Boolean = true,
    val snackbarMessage: String? = null,
    val popularSpaces : List<SpaceInfo> = emptyList(),
    val userSpaces : List<SpaceInfo> = emptyList(),
    val popularSpacesPage : Int = 0,
    val userSpacesPage : Int = 0,
    val hasMorePopularSpaces : Boolean = true,
    val hasMoreUserSpaces : Boolean = true,
    val isLoadingMorePopularSpaces : Boolean = false,
    val isLoadingMoreUserSpaces : Boolean = false
)

sealed class SpaceEvent {
    object SnackbarMessageShown : SpaceEvent()
    data class JoinOrLeave(val spaceId: Long, val join : Boolean, val isPopularSpaces: Boolean) : SpaceEvent()
    data class LoadMoreSpaces(val isPopularSpaces: Boolean) : SpaceEvent()
}

@HiltViewModel
class SpaceViewModel @Inject constructor(
    private val spaceRepository: SpaceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpaceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSpaces()
    }

    fun handleEvent(event: SpaceEvent){
        when(event){
            is SpaceEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is SpaceEvent.JoinOrLeave -> {
                joinOrLeave(event.spaceId, event.join, event.isPopularSpaces)
            }
            is SpaceEvent.LoadMoreSpaces -> {
                loadMoreSpaces(event.isPopularSpaces)
            }
        }
    }

    fun loadSpaces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val popularSpacesJob = launch {
                spaceRepository.getPopularSpaces().collect { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
                            _uiState.update { it.copy(popularSpaces = result.data) }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                        }
                    }
                }
            }

            val userSpacesJob = launch {
                spaceRepository.getUserSpaces().collect { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
                            _uiState.update { it.copy(userSpaces = result.data) }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                        }
                    }
                }
            }

            // 等待所有请求完成
            popularSpacesJob.join()
            userSpacesJob.join()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun joinOrLeave(id: Long, join: Boolean, isPopularSpaces: Boolean) {
        viewModelScope.launch {
            val repository = if (join) {
                spaceRepository.leaveSpace(id)
            } else {
                spaceRepository.joinSpace(id)
            }

            repository.collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            // 根据isPopularSpaces参数决定更新哪个列表
                            if (isPopularSpaces) {
                                // 只更新popularSpaces列表
                                val updatedPopularSpaces = currentState.popularSpaces.map { space ->
                                    if (space.space.id == id) space.copy(isMember = !join) else space
                                }
                                currentState.copy(popularSpaces = updatedPopularSpaces)
                            } else {
                                // 只更新userSpaces列表
                                val updatedUserSpaces = currentState.userSpaces.map { space ->
                                    if (space.space.id == id) space.copy(isMember = !join) else space
                                }
                                currentState.copy(userSpaces = updatedUserSpaces)
                            }
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.message) }
                    }
                }
            }
        }
    }

    fun loadMoreSpaces(isPopularSpaces: Boolean) {
        val currentState = _uiState.value

        // 检查是否有更多数据可加载以及当前是否正在加载
        if (isPopularSpaces) {
            if (!currentState.hasMorePopularSpaces || currentState.isLoadingMorePopularSpaces) {
                return
            }
            _uiState.update { it.copy(isLoadingMorePopularSpaces = true) }
        } else {
            if (!currentState.hasMoreUserSpaces || currentState.isLoadingMoreUserSpaces) {
                return
            }
            _uiState.update { it.copy(isLoadingMoreUserSpaces = true) }
        }

        viewModelScope.launch {
            if (isPopularSpaces) {
                val nextPage = currentState.popularSpacesPage + 1
                spaceRepository.getPopularSpaces(nextPage).collect { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
                            val newSpaces = result.data
                            _uiState.update { it.copy(
                                popularSpaces = it.popularSpaces + newSpaces,
                                popularSpacesPage = nextPage,
                                hasMorePopularSpaces = newSpaces.isNotEmpty(),
                                isLoadingMorePopularSpaces = false
                            )}
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(
                                snackbarMessage = result.exception.message,
                                isLoadingMorePopularSpaces = false
                            )}
                        }
                    }
                }
            } else {
                val nextPage = currentState.userSpacesPage + 1
                spaceRepository.getUserSpaces(nextPage).collect { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
                            val newSpaces = result.data
                            _uiState.update { it.copy(
                                userSpaces = it.userSpaces + newSpaces,
                                userSpacesPage = nextPage,
                                hasMoreUserSpaces = newSpaces.isNotEmpty(),
                                isLoadingMoreUserSpaces = false
                            )}
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(
                                snackbarMessage = result.exception.message,
                                isLoadingMoreUserSpaces = false
                            )}
                        }
                    }
                }
            }
        }
    }
}
