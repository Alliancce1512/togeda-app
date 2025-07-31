package com.togeda.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.togeda.app.domain.model.Event

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

            // Event images gallery
            if (event.images.isNotEmpty()) {
                EventImageGallery(images = event.images)
            } else {
                // Placeholder when no images
                Box(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            color = colorScheme.tertiary,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment    = Alignment.Center
                ) {
                    Icon(
                        modifier            = Modifier.size(48.dp),
                        imageVector         = Icons.Default.Image,
                        contentDescription  = "No event image",
                        tint                = colorScheme.onTertiary
                    )
                }
            }

            // Action icons
            Row(
                modifier                = Modifier.fillMaxWidth(),
                verticalAlignment       = Alignment.CenterVertically,
                horizontalArrangement   = Arrangement.spacedBy(8.dp)
            ) {

                IconButton(onClick = {}) {
                    Icon(
                        imageVector         = Icons.Default.AccountCircle,
                        contentDescription  = "Attendees",
                        tint                = colorScheme.onSurface
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector         = Icons.Default.LocationOn,
                        contentDescription  = "Location",
                        tint                = colorScheme.onSurface
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector         = Icons.AutoMirrored.Filled.Send,
                        contentDescription  = "Share",
                        tint                = colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Bookmark icon on the right
                IconButton(onClick = onBookmarkClick) {
                    Icon(
                        imageVector         = Icons.Default.BookmarkBorder,
                        contentDescription  = "Bookmark",
                        tint                = colorScheme.onSurface
                    )
                }
            }
            
            // Event details
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // First row
                Row(
                    modifier                = Modifier.fillMaxWidth(),
                    horizontalArrangement   = Arrangement.spacedBy(8.dp)
                ) {
                    // Free tag
                    EventTag(
                        text = if (event.isFree) "Free" else "Paid",
                        icon = Icons.AutoMirrored.Filled.EventNote
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
                        icon = Icons.AutoMirrored.Filled.Send,
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
private fun EventImageGallery(images: List<String>) {
    if (images.size == 1) {
        AsyncImage(
            modifier            = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            model               = images[0],
            contentDescription  = "Event image",
            contentScale        = ContentScale.Crop
        )
    } else {
        Box {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 0.dp)
            ) {
                items(images) { imageUrl ->
                    AsyncImage(
                        modifier            = Modifier
                            .size(width = 280.dp, height = 200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        model               = imageUrl,
                        contentDescription  = "Event image",
                        contentScale        = ContentScale.Crop
                    )
                }
            }
            
            // Image counter indicator (top-right corner)
            if (images.size > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text        = "1/${images.size}",
                        color       = Color.White,
                        fontSize    = 12.sp,
                        fontWeight  = FontWeight.Medium
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