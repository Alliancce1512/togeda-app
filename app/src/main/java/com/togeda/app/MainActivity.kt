package com.togeda.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.togeda.app.presentation.navigation.AppNavigation
import com.togeda.app.ui.theme.TogedaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TogedaTheme {
                AppNavigation(modifier = Modifier.statusBarsPadding())
            }
        }
    }
}