package com.postopia.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun snackbarMessageShown() {
        _snackbarMessage.value = null
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}
