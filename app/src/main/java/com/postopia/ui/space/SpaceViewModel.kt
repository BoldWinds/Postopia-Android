package com.postopia.ui.space

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.data.model.SpaceInfo
import com.postopia.domain.repository.SpaceRepository
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
    val hasMorePopularSpaces : Boolean = false,
    val hasMoreUserSpaces : Boolean = false,
    val isLoadingMorePopularSpaces : Boolean = false,
    val isLoadingMoreUserSpaces : Boolean = false,
    val isRefreshing: Boolean = false,
)

sealed class SpaceEvent {
    object SnackbarMessageShown : SpaceEvent()
    object Refresh : SpaceEvent()
    data class JoinOrLeave(val spaceId: Long, val join : Boolean) : SpaceEvent()
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
                joinOrLeave(event.spaceId, event.join)
            }
            is SpaceEvent.LoadMoreSpaces -> {
                loadMoreSpaces(event.isPopularSpaces)
            }
            is SpaceEvent.Refresh -> {
                _uiState.update { it.copy(
                    isRefreshing = true,
                    popularSpacesPage = 0,
                    userSpacesPage = 0,
                    popularSpaces = emptyList(),
                    userSpaces = emptyList(),
                    hasMorePopularSpaces = false,
                    hasMoreUserSpaces = false) }
                loadSpaces()
                _uiState.update { it.copy(isRefreshing = false) }
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

    fun joinOrLeave(id: Long, leave: Boolean) {
        viewModelScope.launch {
            val repository = if (leave) {
                spaceRepository.leaveSpace(id)
            } else {
                spaceRepository.joinSpace(id)
            }

            repository.collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val updatedPopularSpaces = currentState.popularSpaces.map { spaceInfo ->
                                if (spaceInfo.space.id == id) {
                                    spaceInfo.copy(isMember = !leave)
                                } else {
                                    spaceInfo
                                }
                            }

                            val updatedUserSpaces = if (leave) {
                                currentState.userSpaces.filter { it.space.id != id }
                            } else {
                                val joinSpace = updatedPopularSpaces.find { it.space.id == id }
                                currentState.userSpaces + joinSpace
                                if(joinSpace != null) {
                                    currentState.userSpaces + joinSpace
                                } else {
                                    currentState.userSpaces
                                }
                            }

                            currentState.copy(
                                popularSpaces = updatedPopularSpaces,
                                userSpaces = updatedUserSpaces,
                            )
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
                                hasMorePopularSpaces = newSpaces.size >= 20,
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
                                hasMoreUserSpaces = newSpaces.size >= 20,
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
