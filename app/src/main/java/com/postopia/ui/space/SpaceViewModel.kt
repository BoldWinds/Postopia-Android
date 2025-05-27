package com.postopia.ui.space

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.data.model.SpaceInfo
import com.postopia.data.model.SpacePart
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
    val userSpaces : List<SpacePart> = emptyList(),
    val spaceInfo : SpaceInfo? = null,
)

sealed class SpaceEvent {
    object SnackbarMessageShown : SpaceEvent()
    data class JoinOrLeave(val spaceId: Long, val join : Boolean) : SpaceEvent()
    data class LoadSpaceDetail(val spaceId: Long) : SpaceEvent()
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
            is SpaceEvent.LoadSpaceDetail -> {
                if(_uiState.value.spaceInfo != null && _uiState.value.spaceInfo!!.space.id == event.spaceId) return
                loadSpaceDetail(event.spaceId)
            }
        }
    }

    fun loadSpaces() {
        viewModelScope.launch {
            spaceRepository.getPopularSpaces().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(popularSpaces = result.data) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = result.exception.message) }
                    }
                }
            }
            spaceRepository.getUserSpaces().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(userSpaces = result.data) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
        _uiState.update { it.copy(isLoading = false) }
    }

    fun joinOrLeave(id : Long, join : Boolean) {
        // TODO join/leave space
    }

    fun loadSpaceDetail(spaceId: Long) {
        viewModelScope.launch {
            spaceRepository.getSpace(spaceId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(spaceInfo = result.data, isLoading = false) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = result.message) }
                    }
                }
            }
        }
    }
}