package com.postopia.ui.profile

import androidx.lifecycle.ViewModel
import com.postopia.data.model.UserDetail
import com.postopia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val isLoading: Boolean = true,
    val userDetail: UserDetail? = null,
    val snackbarMessage: String? = null,
)

sealed class ProfileEvent {
    data class LoadUserProfile(val userId: String? = null) : ProfileEvent()
    object RefreshProfile : ProfileEvent()
    data class UpdateProfile(val updatedUserDetail: UserDetail) : ProfileEvent()
    object SnackbarMessageShown : ProfileEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadUserProfile -> loadUserProfile()
            is ProfileEvent.RefreshProfile -> refreshProfile()
            is ProfileEvent.UpdateProfile -> updateProfile(event.updatedUserDetail)
            is ProfileEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) } // Clear snackbar message
            }
        }
    }

    fun loadUserProfile() {
        _uiState.update { it.copy(isLoading = false) }
    }

    fun refreshProfile() {
    }

    fun updateProfile(updatedUserDetail: UserDetail) {

    }
}
