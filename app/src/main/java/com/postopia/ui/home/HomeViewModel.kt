package com.postopia.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.domain.mapper.PostMapper.toPostCardInfo
import com.postopia.domain.repository.PostRepository
import com.postopia.ui.model.PostCardInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,
    val spaceInfos : List<PostCardInfo> = emptyList<PostCardInfo>(),
    val isLoadingMore : Boolean = false,
    val page : Int = 0,
    val hasMore : Boolean = false,
    val isRefreshing: Boolean = false,
)

sealed class HomeEvent {
    object SnackbarMessageShown : HomeEvent()
    object LoadMorePosts : HomeEvent()
    object Refresh: HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPopularPosts()
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is HomeEvent.LoadMorePosts -> {
                loadPopularPosts(_uiState.value.page)
            }
            is HomeEvent.Refresh -> {
                _uiState.update { it.copy(isRefreshing = true, page = 0, spaceInfos = emptyList(), hasMore = false) }
                loadPopularPosts(0)
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    fun loadPopularPosts(page: Int = 0){
        viewModelScope.launch {
            postRepository.getPopularPosts(page).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoadingMore = true) }
                    }
                    is Result.Success -> {
                        val newSpaceInfos = result.data
                        _uiState.update {
                            it.copy(
                                spaceInfos =  it.spaceInfos + newSpaceInfos.map{ it.toPostCardInfo() },
                                isLoadingMore = false,
                                page = page + 1,
                                hasMore = newSpaceInfos.size >= 20
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            snackbarMessage = result.exception.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }
}