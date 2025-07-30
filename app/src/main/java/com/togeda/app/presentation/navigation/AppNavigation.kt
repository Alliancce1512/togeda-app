package com.togeda.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.togeda.app.presentation.feed.FeedScreen
import com.togeda.app.presentation.login.LoginScreen

sealed class Screen {
    object Login : Screen()
    object Feed : Screen()
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    
    when (currentScreen) {
        Screen.Login -> {
            LoginScreen(
                onNavigateToFeed = {
                    currentScreen = Screen.Feed
                },
                modifier = modifier
            )
        }
        Screen.Feed -> {
            FeedScreen(
                modifier = modifier
            )
        }
    }
} 