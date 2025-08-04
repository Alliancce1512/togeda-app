package com.togeda.app.presentation.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class EventTag(
    val text            : String,
    val icon            : ImageVector?,
    val backgroundColor : Color = Color(0xFF444444),
    val textColor       : Color = Color.White,
    val iconColor       : Color = Color.White
) 