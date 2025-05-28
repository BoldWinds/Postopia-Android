package com.postopia.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.data.model.UserDetail
import com.postopia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val selectedTab : Int = 0,
    val userDetail: UserDetail? = null,
    val snackbarMessage: String? = null,
)

sealed class ProfileEvent {
    data class ChangeTab(val tabIndex: Int) : ProfileEvent()
    object SnackbarMessageShown : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
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
            is ProfileEvent.ChangeTab -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
            }
        }
    }

    fun loadUserProfile() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userDetail = result.data,
                                snackbarMessage = "Profile loaded successfully"
                            )
                        }
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
}
