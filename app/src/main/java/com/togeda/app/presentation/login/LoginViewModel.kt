package com.togeda.app.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.togeda.app.R
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

    fun onForgotPasswordClick() {
        // TODO: Navigate to forgot password screen
    }

    fun onLoginClick() {
        val currentState = _state.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.update { 
                it.copy(loginState = UiState.Error("Email and password cannot be empty"))
            }
            return
        }

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Check against hardcoded credentials
                if (currentState.email == "test" && currentState.password == "test") {
                    _state.update { 
                        it.copy(
                            loginState = UiState.Success(
                                com.togeda.app.domain.model.User(
                                    id = "1",
                                    email = currentState.email,
                                    name = "Togeda User",
                                    token = "mock_token_123"
                                )
                            ),
                            isLoading = false
                        )
                    }
                } else {
                    _state.update { 
                        it.copy(
                            loginState = UiState.Error("Invalid email or password"),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        loginState = UiState.Error(e.message ?: "Login failed"),
                        isLoading = false
                    )
                }
            }
        }
    }
}
