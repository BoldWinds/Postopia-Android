package com.postopia.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val isRegister : Boolean = false,    // true为注册，false为登陆
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class AuthEvent {
    data class UsernameChanged(val username: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthEvent()
    object Register : AuthEvent()
    object Login : AuthEvent()
    object ClearError : AuthEvent()
    object ChangeAuth : AuthEvent()
}

@HiltViewModel
class AuthViewModel @Inject constructor(

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
                register()
            }
            is AuthEvent.Login -> {
                login()
            }
            is AuthEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is AuthEvent.ChangeAuth ->{
                _uiState.update { it.copy(isRegister = !it.isRegister) }
            }
        }
    }

    private fun register() {
        val state = _uiState.value

        // 输入验证
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "用户名和密码不能为空") }
            return
        }

        if(state.username.contains(';')){
            _uiState.update { it.copy(errorMessage = "用户名不能包含分号") }
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "密码不匹配") }
            return
        }

        // 设置加载状态
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // TODO 实现注册逻辑
    }

    private fun login() {
        val state = _uiState.value

        // 输入验证
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "用户名和密码不能为空") }
            return
        }

        if(state.username.contains(';')){
            _uiState.update { it.copy(errorMessage = "用户名不能包含分号") }
            return
        }

        // 设置加载状态
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        // TODO 实现登陆
    }
}