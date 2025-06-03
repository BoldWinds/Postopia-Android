package com.postopia.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.domain.mapper.CommentMapper.toUiModel
import com.postopia.domain.mapper.PostMapper.toPostCardInfo
import com.postopia.domain.mapper.ProfileMapper.toUiModel
import com.postopia.domain.mapper.SpaceMapper.toUiModel
import com.postopia.domain.repository.SearchRepository
import com.postopia.ui.model.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val snackbarMessage: String? = null,
    val searchType: SearchType = SearchType.POST,
    val query: String = "",
    val selectedTab: Int = 0,
    val searchResults: List<Any> = emptyList(),
    val isLoadingResults : Boolean = false,
    val hasMoreResults: Boolean = false,
    val currentPage: Int = 0,
)

sealed class SearchEvent {
    object SnackbarMessageShown : SearchEvent()
    data class NewSearch(val query: String, val tabIndex: Int) : SearchEvent()
    object LoadMoreResults : SearchEvent()
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is SearchEvent.NewSearch -> {
                _uiState.update {
                    it.copy(
                        query = event.query,
                        selectedTab = event.tabIndex,
                        searchType = when (event.tabIndex) {
                            0 -> SearchType.POST
                            1 -> SearchType.COMMENT
                            2 -> SearchType.SPACE
                            else -> SearchType.USER
                        },
                        isLoadingResults = false,
                        hasMoreResults = false,
                        currentPage = 0,
                        searchResults = emptyList(),
                    )
                }
                performSearch(_uiState.value.searchType, event.query)
            }
            is SearchEvent.LoadMoreResults -> {
                performSearch(_uiState.value.searchType, _uiState.value.query)
            }
        }
    }

    fun performSearch(searchType: SearchType, query: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingResults = true) }
            val currentPage = _uiState.value.currentPage
            when (searchType) {
                SearchType.POST -> {
                    searchRepository.searchPosts(query, currentPage).collect { result->
                        when (result) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                val newResults = result.data.map{ it.toPostCardInfo()}
                                _uiState.update { state ->
                                    state.copy(
                                        searchResults = if (state.currentPage == 0) newResults else state.searchResults + newResults,
                                        isLoadingResults = false,
                                        hasMoreResults = newResults.size == 20,
                                        currentPage = state.currentPage + 1
                                    )
                                }
                            }
                            is Result.Error -> {
                                _uiState.update { it.copy(snackbarMessage = result.message, isLoadingResults = false, hasMoreResults = false) }
                            }
                        }
                    }
                }
                SearchType.USER -> {
                    searchRepository.searchUsers(query, currentPage).collect { result->
                        when (result) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                val newResults = result.data.map { it.toUiModel() }
                                _uiState.update { state ->
                                    state.copy(
                                        searchResults = if (state.currentPage == 0) newResults else state.searchResults + newResults,
                                        isLoadingResults = false,
                                        hasMoreResults = newResults.size == 20,
                                        currentPage = state.currentPage + 1
                                    )
                                }
                            }
                            is Result.Error -> {
                                _uiState.update { it.copy(snackbarMessage = result.message, isLoadingResults = false, hasMoreResults = false) }
                            }
                        }
                    }
                }
                SearchType.SPACE -> {
                    searchRepository.searchSpaces(query, currentPage).collect { result->
                        when (result) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                val newResults = result.data.map { it.toUiModel() }
                                _uiState.update { state ->
                                    state.copy(
                                        searchResults = if (state.currentPage == 0) newResults else state.searchResults + newResults,
                                        isLoadingResults = false,
                                        hasMoreResults = newResults.size == 20,
                                        currentPage = state.currentPage + 1
                                    )
                                }
                            }
                            is Result.Error -> {
                                _uiState.update { it.copy(snackbarMessage = result.message, isLoadingResults = false, hasMoreResults = false) }
                            }
                        }
                    }
                }
                SearchType.COMMENT -> {
                    searchRepository.searchComments(query, currentPage).collect { result->
                        when (result) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                val newResults = result.data.map{ it.toUiModel() }
                                _uiState.update { state ->
                                    state.copy(
                                        searchResults = if (state.currentPage == 0) newResults else state.searchResults + newResults,
                                        isLoadingResults = false,
                                        hasMoreResults = newResults.size == 20,
                                        currentPage = state.currentPage + 1
                                    )
                                }
                            }
                            is Result.Error -> {
                                _uiState.update { it.copy(snackbarMessage = result.message, isLoadingResults = false, hasMoreResults = false) }
                            }
                        }
                    }
                }
            }
        }
    }

}