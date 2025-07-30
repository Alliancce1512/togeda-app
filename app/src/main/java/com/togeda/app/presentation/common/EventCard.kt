package com.togeda.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.togeda.app.domain.model.Event
import com.togeda.app.ui.theme.*

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
    onEventClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        )
    ) {
        Column(
            modifier            = Modifier.padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with organizer info
            Row(
                modifier                = Modifier.fillMaxWidth(),
                verticalAlignment       = Alignment.Top,
                horizontalArrangement   = Arrangement.SpaceBetween
            ) {
                // Organizer avatar
                Box(
                    modifier            = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorScheme.tertiary),
                    contentAlignment    = Alignment.Center
                ) {
                    Icon(
                        imageVector         = Icons.Default.Person,
                        contentDescription  = "Organizer avatar",
                        tint                = colorScheme.onTertiary
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Event title and organizer
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text        = event.title,
                        fontSize    = 18.sp,
                        fontWeight  = FontWeight.Bold,
                        color       = colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = event.organizer,
                        fontSize = 14.sp,
                        color = colorScheme.tertiary
                    )
                }
                
                // More options button
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = colorScheme.onSurface
                    )
                }
            }
            
            // Event image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Event image",
                    tint = colorScheme.onTertiary,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            // Action icons
            Row(
                modifier            = Modifier.fillMaxWidth(),
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Attendees",
                        tint = colorScheme.onSurface
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = colorScheme.onSurface
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Share",
                        tint = colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Bookmark icon on the right
                IconButton(onClick = onBookmarkClick) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = colorScheme.onSurface
                    )
                }
            }
            
            // Event details
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // First row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Free tag
                    EventTag(
                        text = if (event.isFree) "Free" else "Paid",
                        icon = Icons.Default.EventNote
                    )
                    
                    // Date
                    EventTag(
                        icon = Icons.Default.CalendarToday,
                        text = "${event.startDate} - ${event.endDate}"
                    )
                    
                    // Time
                    EventTag(
                        icon = Icons.Default.Schedule,
                        text = "${event.startTime} - ${event.endTime}"
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Second row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Attendees
                    EventTag(
                        icon = Icons.Default.Group,
                        text = "${event.currentAttendees}/${event.maxAttendees}"
                    )
                    
                    // Location
                    EventTag(
                        icon = Icons.Default.Send,
                        text = event.location
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Third row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Public tag
                    EventTag(
                        text = if (event.isPublic) "Public" else "Private",
                        icon = Icons.Default.Public
                    )
                    
                    // Confirm Location tag
                    EventTag(
                        text = if (event.locationConfirmed) "Location Confirmed" else "Confirm Location",
                        icon = Icons.Default.ShareLocation
                    )
                }
            }
        }
    }
}

@Composable
private fun EventTag(
    text            : String,
    backgroundColor : Color = Color(0xFF444444),
    textColor       : Color = Color.White,
    iconColor       : Color = Color.White,
    icon            : ImageVector,
    modifier        : Modifier = Modifier
) {
    Row(
        modifier                = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(all = 6.dp),
        verticalAlignment       = Alignment.CenterVertically,
        horizontalArrangement   = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            modifier            = Modifier.size(16.dp),
            imageVector         = icon,
            contentDescription  = null,
            tint                = iconColor
        )

        Text(
            text            = text,
            fontSize        = 12.sp,
            color           = textColor,
            fontWeight      = FontWeight.Medium
        )
    }
}