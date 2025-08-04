package com.togeda.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun InfoTag(
    text            : String,
    backgroundColor : Color         = Color(0xFF444444),
    textColor       : Color         = Color.White,
    iconColor       : Color         = Color.White,
    icon            : ImageVector?  = null,
    iconUrl         : String?       = null,
    emoji           : String?       = null,
    modifier        : Modifier      = Modifier
) {
    Row(
        modifier                = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(all = 6.dp),
        verticalAlignment       = Alignment.CenterVertically,
        horizontalArrangement   = Arrangement.spacedBy(4.dp)
    ) {
        when {
            !emoji.isNullOrEmpty() -> {
                // Display emoji character
                Text(
                    text        = emoji,
                    fontSize    = 16.sp,
                    modifier    = Modifier.size(16.dp)
                )
            }
            !iconUrl.isNullOrEmpty() && iconUrl.startsWith("http") -> {
                // Use AsyncImage for actual URL images
                AsyncImage(
                    modifier            = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    model               = iconUrl,
                    contentDescription  = null,
                    contentScale        = ContentScale.Crop
                )
            }
            icon != null -> {
                // Use ImageVector icon
                Icon(
                    modifier            = Modifier.size(16.dp),
                    imageVector         = icon,
                    contentDescription  = null,
                    tint                = iconColor
                )
            }
        }

        Text(
            text            = text,
            fontSize        = 12.sp,
            color           = textColor,
            fontWeight      = FontWeight.Medium
        )
    }
} 