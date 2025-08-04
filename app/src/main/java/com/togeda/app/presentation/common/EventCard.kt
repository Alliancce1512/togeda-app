package com.togeda.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.Star
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
    event           : Event,
    modifier        : Modifier      = Modifier,
    onEventClick    : () -> Unit    = {},
    onBookmarkClick : () -> Unit    = {},
    onMoreClick     : () -> Unit    = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Card(
        modifier    = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onEventClick() },
        shape       = RoundedCornerShape(16.dp),
        colors      = CardDefaults.cardColors(containerColor = colorScheme.surface)
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
                if (!event.organizerAvatar.isNullOrEmpty()) {
                    AsyncImage(
                        modifier            = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        model               = event.organizerAvatar,
                        contentDescription  = "Organizer avatar",
                        contentScale        = ContentScale.Crop
                    )
                } else {
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
                }
                
                Spacer(modifier = Modifier.width(12.dp))

                // Event title and organizer
                Column(modifier = Modifier.weight(1f)) {
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
                val tags = generateEventTags(event)
                
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    modifier                = Modifier.fillMaxWidth(),
                    horizontalArrangement   = Arrangement.spacedBy(8.dp),
                    verticalArrangement     = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { eventTag ->
                        InfoTag(
                            text            = eventTag.text,
                            icon            = eventTag.icon,
                            backgroundColor = eventTag.backgroundColor,
                            textColor       = eventTag.textColor,
                            iconColor       = eventTag.iconColor
                        )
                    }
                }
            }
        }
    }
}

data class EventTag(
    val text            : String,
    val icon            : ImageVector?,
    val backgroundColor : Color = Color(0xFF444444),
    val textColor       : Color = Color.White,
    val iconColor       : Color = Color.White
)

private fun generateEventTags(event: com.togeda.app.domain.model.Event): List<EventTag> {
    val tags = mutableListOf<EventTag>()
    
    // Date and Time tags (only if not TBD)
    if (event.startDate != "TBD" && event.endDate != "TBD") {
        tags.add(EventTag("${event.startDate} - ${event.endDate}", Icons.Default.CalendarToday))
    }
    if (event.startTime != "TBD" && event.endTime != "TBD") {
        tags.add(EventTag("${event.startTime} - ${event.endTime}", Icons.Default.Schedule))
    }
    
    // Location tag (only if not TBD)
    if (event.location != "Location TBD") {
        tags.add(EventTag(event.location, Icons.Default.LocationOn))
    }
    
    // Attendees tag (only if max attendees > 0)
    if (event.maxAttendees > 0) {
        tags.add(EventTag("${event.currentAttendees}/${event.maxAttendees}", Icons.Default.Group))
    }
    
    // Status tag (only for non-active events)
    when (event.status.uppercase()) {
        "CANCELLED" -> tags.add(EventTag("Cancelled", Icons.Default.Cancel))
        "COMPLETED" -> tags.add(EventTag("Completed", Icons.Default.Done))
        "DRAFT" -> tags.add(EventTag("Draft", Icons.Default.Edit))
        else -> {} // Don't show "Active" status as it's the default
    }
    
    // User status tag (only for participating users)
    when (event.currentUserStatus.uppercase()) {
        "PARTICIPATING" -> tags.add(EventTag("Participating", Icons.Default.Person))
        "PENDING" -> tags.add(EventTag("Pending", Icons.Default.Schedule))
        "BLOCKED" -> tags.add(EventTag("Blocked", Icons.Default.Block))
        else -> {} // Don't show "Not Participating" as it's the default
    }
    
    // Payment tag
    if (event.isFree) {
        tags.add(EventTag("Free", Icons.AutoMirrored.Filled.EventNote))
    } else {
        tags.add(EventTag("Paid", Icons.Default.AttachMoney))
    }
    
    // Accessibility tag
    if (event.isPublic) {
        tags.add(EventTag("Public", Icons.Default.Public))
    } else {
        tags.add(EventTag("Private", Icons.Default.Lock))
    }
    
    // Location confirmation tag
    if (event.locationConfirmed) {
        tags.add(EventTag("Location Confirmed", Icons.Default.ShareLocation))
    } else {
        tags.add(EventTag("Confirm Location", Icons.Default.ShareLocation))
    }
    
    // Ask to join tag
    if (event.askToJoin) {
        tags.add(EventTag("Approval Required", Icons.Default.Security))
    }
    
    // Blocked tag
    if (event.blockedForCurrentUser) {
        tags.add(EventTag("Blocked", Icons.Default.Block))
    }
    
    // Rating tag (if available)
    event.rating?.let { rating ->
        if (rating > 0) {
            tags.add(EventTag("★ ${String.format("%.1f", rating)}", Icons.Default.Star))
        }
    }
    
    // Saved tag
    if (event.savedByCurrentUser) {
        tags.add(EventTag("Saved", Icons.Default.Bookmark))
    }
    
    return tags
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