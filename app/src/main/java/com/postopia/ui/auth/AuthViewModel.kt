package com.postopia.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isRegister : Boolean = false,    // true为注册，false为登陆
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null
)

sealed class AuthEvent {
    data class UsernameChanged(val username: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthEvent()
    object Register : AuthEvent()
    object Login : AuthEvent()
    object ChangeAuth : AuthEvent()
    object SnackbarMessageShown : AuthEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = event.username) }
            }
            is AuthEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is AuthEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
            }
            is AuthEvent.Register -> {
                register(_uiState.value.username, _uiState.value.password, _uiState.value.confirmPassword)
            }
            is AuthEvent.Login -> {
                login(_uiState.value.username, _uiState.value.password)
            }
            is AuthEvent.ChangeAuth -> {
                _uiState.update { it.copy(isRegister = !it.isRegister, snackbarMessage = null) }
            }
            is AuthEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
        }
    }

    private fun register(username : String, password : String, confirmPassword : String) {
        // 输入验证
        if (username.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "用户名和密码不能为空") }
            return
        }

        if(username.contains(';')){
            _uiState.update { it.copy(snackbarMessage = "用户名不能包含分号") }
            return
        }

        if (password != confirmPassword) {
            _uiState.update { it.copy(snackbarMessage = "密码不匹配") }
            return
        }

        // 设置加载状态
        _uiState.update { it.copy(isLoading = true, snackbarMessage = null) }

        viewModelScope.launch {
            authRepository.register(username, password).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = "注册成功！请登录。", isRegister = false) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = result.message) }
                    }
                }
            }
        }
    }

    private fun login(username : String, password : String) {
        // 输入验证
        if (username.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "用户名和密码不能为空") }
            return
        }

        if(username.contains(';')){
            _uiState.update { it.copy(snackbarMessage = "用户名不能包含分号") }
            return
        }

        // 设置加载状态
        _uiState.update { it.copy(isLoading = true, snackbarMessage = null) }

        viewModelScope.launch {
            authRepository.login(username, password).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, snackbarMessage = null) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = "登陆成功！") }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = result.message) }
                    }
                }
            }
        }
    }
}
