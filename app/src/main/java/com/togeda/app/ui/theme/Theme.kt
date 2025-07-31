package com.togeda.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary     = White,
    onPrimary   = Black,
    background  = Black,
    surface     = GrayField,
    onSurface   = White,
    secondary   = BlueLink,
    onSecondary = White,
    tertiary    = GrayText,
    onTertiary  = White
)

private val LightColorScheme = lightColorScheme(
    primary     = Black,
    onPrimary   = White,
    background  = White,
    surface     = GrayField,
    onSurface   = Black,
    secondary   = BlueLink,
    onSecondary = White,
    tertiary    = GrayText,
    onTertiary  = Black
)

@Composable
fun TogedaTheme(
    darkTheme   : Boolean = isSystemInDarkTheme(),
    content     : @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography  = Typography,
        content     = content
    )
}