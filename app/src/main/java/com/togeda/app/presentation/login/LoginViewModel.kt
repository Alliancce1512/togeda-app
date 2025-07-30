package com.togeda.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.togeda.app.domain.usecase.LoginUseCase
import com.togeda.app.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onPasswordVisibilityToggle() {
        _state.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onLoginClick() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loginState = UiState.Loading) }
            try {
                val result = loginUseCase(currentState.email, currentState.password)
                result.fold(
                    onSuccess = { user ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                loginState = UiState.Success(user)
                            )
                        }
                    },
                    onFailure = { exception ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                loginState = UiState.Error(exception.message ?: "Login failed")
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        loginState = UiState.Error(e.message ?: "Unknown error occurred")
                    )
                }
            }
        }
    }

    fun onForgotPasswordClick() {
        // Handle forgot password navigation
    }
}
