package com.togeda.app.presentation.login

import com.togeda.app.domain.model.User
import com.togeda.app.presentation.common.UiState

data class LoginState(
    val email           : String        = "",
    val password        : String        = "",
    val passwordVisible : Boolean       = false,
    val loginState      : UiState<User> = UiState.Idle,
    val isLoading       : Boolean       = false
)

