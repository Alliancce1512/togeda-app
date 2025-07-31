package com.togeda.app.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.togeda.app.domain.repository.AuthRepository
import com.togeda.app.presentation.feed.FeedScreen
import com.togeda.app.presentation.login.LoginScreen
import com.togeda.app.presentation.login.LoginViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

sealed class Screen {
    object Login    : Screen()
    object Feed     : Screen()
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    var currentScreen by remember { mutableStateOf<Screen?>(null) }
    val authRepository = get<AuthRepository>()

    LaunchedEffect(Unit) {
        authRepository.isLoggedIn().collect { isLoggedIn ->
            currentScreen = if (isLoggedIn) Screen.Feed else Screen.Login
        }
    }
    
    when (currentScreen) {
        Screen.Login    -> {
            val loginViewModel: LoginViewModel = koinViewModel()

            LaunchedEffect(currentScreen) {
                loginViewModel.resetLoginState()
            }

            LoginScreen(
                viewModel   = loginViewModel,
                modifier    = modifier
            )
        }
        Screen.Feed     -> {
            FeedScreen(
                modifier = modifier
            )
        }
        null            -> {
            // Loading state
            Box(
                modifier            = Modifier.fillMaxSize(),
                contentAlignment    = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
} 