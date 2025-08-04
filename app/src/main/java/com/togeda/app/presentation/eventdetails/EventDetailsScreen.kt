package com.togeda.app.presentation.eventdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Cabin
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.togeda.app.R
import com.togeda.app.domain.model.Event
import com.togeda.app.domain.model.Interest
import com.togeda.app.presentation.common.InfoTag
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventDetailsScreen(
    eventId         : String,
    onBackClick     : () -> Unit,
    onMoreClick     : () -> Unit,
    onJoinClick     : () -> Unit,
    onShareClick    : () -> Unit,
    onBookmarkClick : () -> Unit,
    modifier        : Modifier = Modifier
) {
    val viewModel: EventDetailsViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val statusBarHeight = with(LocalDensity.current) { 24.dp }

    LaunchedEffect(eventId) {
        viewModel.onEvent(EventDetailsEvent.LoadEvent(eventId))
    }

    // Handle device back button
    BackHandler(enabled = true) { onBackClick() }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                Box(
                    modifier            = Modifier.fillMaxSize(),
                    contentAlignment    = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(
                    modifier            = Modifier.fillMaxSize(),
                    contentAlignment    = Alignment.Center
                ) {
                    Text(
                        text    = state.error ?: "Unknown error",
                        color   = Color.White
                    )
                }
            }
            state.event != null -> {
                EventDetailsContent(event = state.event!!)

                // Top navigation buttons overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = statusBarHeight + 16.dp, start = 16.dp, end = 16.dp)
                        .align(Alignment.TopCenter)
                ) {
                    Row(
                        modifier                = Modifier.fillMaxWidth(),
                        horizontalArrangement   = Arrangement.SpaceBetween,
                        verticalAlignment       = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier    = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape
                                ),
                            onClick     = onBackClick
                        ) {
                            Icon(
                                imageVector         = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription  = "Back",
                                tint                = Color.White
                            )
                        }
                        IconButton(
                            modifier    = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape
                                ),
                            onClick     = onMoreClick
                        ) {
                            Icon(
                                imageVector         = Icons.Default.MoreVert,
                                contentDescription  = "More options",
                                tint                = Color.White
                            )
                        }
                    }
                }

                // Bottom action bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Row(
                        modifier                = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1A1A1A))
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement   = Arrangement.spacedBy(12.dp),
                        verticalAlignment       = Alignment.CenterVertically
                    ) {
                        // Join button
                        Button(
                            modifier    = Modifier.weight(1f),
                            onClick     = {
                                viewModel.onEvent(EventDetailsEvent.JoinEvent)
                                onJoinClick()
                            },
                            colors      = ButtonDefaults.buttonColors(
                                containerColor  = Color.White,
                                contentColor    = Color.Black
                            ),
                            shape       = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text        = stringResource(R.string.join),
                                fontSize    = 16.sp,
                                fontWeight  = FontWeight.Medium
                            )
                        }

                        // Share button
                        IconButton(
                            modifier    = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color(0xFF2A2A2A),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            onClick     = {
                                viewModel.onEvent(EventDetailsEvent.ShareEvent)
                                onShareClick()
                            }
                        ) {
                            Icon(
                                imageVector         = Icons.AutoMirrored.Filled.Send,
                                contentDescription  = "Share",
                                tint                = Color.White
                            )
                        }
                        // Bookmark button
                        IconButton(
                            modifier    = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color(0xFF2A2A2A),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            onClick     = {
                                viewModel.onEvent(EventDetailsEvent.ToggleBookmark)
                                onBookmarkClick()
                            }
                        ) {
                            Icon(
                                imageVector         = Icons.Default.BookmarkBorder,
                                contentDescription  = "Bookmark",
                                tint                = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDetailsContent(event: Event) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(rememberScrollState())
    ) {
        // Header with image (no nav buttons here)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            if (event.images.isNotEmpty()) {
                AsyncImage(
                    modifier            = Modifier.fillMaxSize(),
                    model               = event.images[0],
                    contentDescription  = "Event image",
                    contentScale        = ContentScale.Crop
                )
            } else {
                Box(
                    modifier            = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF2C2C2C)),
                    contentAlignment    = Alignment.Center
                ) {
                    Icon(
                        modifier            = Modifier.size(64.dp),
                        imageVector         = Icons.Default.Person,
                        contentDescription  = "No event image",
                        tint                = Color.White
                    )
                }
            }
        }

        // Event title and organizer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text        = event.title,
                fontSize    = 24.sp,
                fontWeight  = FontWeight.Bold,
                color       = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Organizer info
            Row(verticalAlignment = Alignment.CenterVertically) {
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

                Column {
                    Text(
                        text        = event.organizer,
                        fontSize    = 16.sp,
                        fontWeight  = FontWeight.Medium,
                        color       = Color.White
                    )
                    Text(
                        text        = event.organizerOccupation ?: "Organizer",
                        fontSize    = 14.sp,
                        color       = Color.Gray
                    )
                }
            }
        }

        // Event details cards
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Date and Time card
            EventDetailCard(
                icon        = Icons.Default.CalendarToday,
                title       = "${event.startDate} - ${event.endDate}",
                subtitle    = "${event.startTime} - ${event.endTime}"
            )

            // Location card
            EventDetailCard(
                icon        = Icons.Default.LocationOn,
                title       = event.location,
                subtitle    = event.locationDetails?.let { "${it.city}, ${it.country}" } ?: event.location
            )

            // Participants card
            EventDetailCard(
                icon        = Icons.Default.Group,
                title       = stringResource(R.string.participants) + " ${event.currentAttendees}/${event.maxAttendees}",
                subtitle    = stringResource(R.string.join_event)
            )

            // Confirm Location card (highlighted)
            EventDetailCard(
                icon        = Icons.Default.ShareLocation,
                title       = stringResource(R.string.confirm_location),
                subtitle    = stringResource(R.string.confirm_location_description),
                iconColor   = Color(0xFFFF6B35),
                borderColor = Color(0xFFFF6B35)
            )

            // Public visibility card
            EventDetailCard(
                icon        = Icons.Default.Public,
                title       = stringResource(R.string.event_public),
                subtitle    = stringResource(R.string.public_description)
            )

            // Cost card
            EventDetailCard(
                icon        = Icons.Default.Person,
                title       = if (event.isFree) stringResource(R.string.free) else stringResource(R.string.paid),
                subtitle    = if (event.isFree) stringResource(R.string.free_description) else stringResource(R.string.paid_description)
            )

            // Description section
            if (!event.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    modifier    = Modifier.padding(bottom = 8.dp),
                    text        = stringResource(R.string.description),
                    fontSize    = 18.sp,
                    fontWeight  = FontWeight.Bold,
                    color       = Color.White
                )
                
                Text(
                    text        = event.description,
                    fontSize    = 14.sp,
                    color       = Color.Gray,
                    lineHeight  = 20.sp
                )
            }

            // Location section
            event.locationDetails?.let {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    modifier    = Modifier.padding(bottom = 8.dp),
                    text        = stringResource(R.string.location),
                    fontSize    = 18.sp,
                    fontWeight  = FontWeight.Bold,
                    color       = Color.White
                )
                
                LocationSection(locationDetails = event.locationDetails)
            }

            // Interests section
            if (event.interests.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    modifier    = Modifier.padding(bottom = 8.dp),
                    text        = stringResource(R.string.interests),
                    fontSize    = 18.sp,
                    fontWeight  = FontWeight.Bold,
                    color       = Color.White
                )
                
                InterestsActionRow(interests = event.interests)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun EventDetailCard(
    icon        : ImageVector,
    title       : String,
    subtitle    : String,
    iconColor   : Color = Color.White,
    borderColor : Color? = null
) {
    Card(
        modifier    = Modifier.fillMaxWidth(),
        colors      = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape       = RoundedCornerShape(12.dp),
        border      = borderColor?.let {
            BorderStroke(width = 1.dp, color = it)
        }
    ) {
        Row(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment   = Alignment.CenterVertically
        ) {
            Icon(
                modifier            = Modifier.size(24.dp),
                imageVector         = icon,
                contentDescription  = null,
                tint                = iconColor
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text        = title,
                    fontSize    = 16.sp,
                    fontWeight  = FontWeight.Medium,
                    color       = Color.White
                )
                Text(
                    modifier    = Modifier.padding(top = 4.dp),
                    text        = subtitle,
                    fontSize    = 14.sp,
                    color       = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InterestsActionRow(interests: List<Interest>) {
    FlowRow(
        modifier                = Modifier.fillMaxWidth(),
        horizontalArrangement   = Arrangement.spacedBy(12.dp),
        verticalArrangement     = Arrangement.spacedBy(12.dp),
    ) {
        interests.forEach { interest ->
            InterestTag(interest = interest)
        }
    }
}

@Composable
private fun InterestTag(interest: Interest) {
    val icon = when (interest.name.lowercase()) {
        "gaming"                -> Icons.Default.SportsEsports
        "outdoor"               -> Icons.Default.Park
        "card games"            -> Icons.Default.Casino
        "events"                -> Icons.Default.Event
        "restaurants"           -> Icons.Default.Restaurant
        "party"                 -> Icons.Default.Celebration
        "networking"            -> Icons.Default.People
        "escape room"           -> Icons.Default.Extension
        "board games"           -> Icons.Default.Casino
        "hanging in the park"   -> Icons.Default.Park
        "chess"                 -> Icons.Default.Casino
        "camping"               -> Icons.Default.Cabin
        "picnic"                -> Icons.Default.Restaurant
        else                    -> Icons.Default.Star
    }
    
    val isEmoji = interest.icon?.let { icon ->
        icon.isNotEmpty() && (
            icon.length == 1 && icon.codePointAt(0) > 127 ||
            icon.length > 1 && icon.all { it.code > 127 }
        )
    } ?: false
    
    InfoTag(
        text        = interest.name,
        icon        = icon,
        iconUrl     = if (!isEmoji) interest.icon else null,
        emoji       = if (isEmoji) interest.icon else null
    )
}

@Composable
private fun LocationSection(locationDetails: com.togeda.app.domain.model.LocationDetails) {
    val locationName = locationDetails.name.ifEmpty {
        buildString {
            if (locationDetails.city.isNotEmpty()) {
                append(locationDetails.city)
            }
            if (locationDetails.state.isNotEmpty()) {
                if (isNotEmpty()) append(", ")
                append(locationDetails.state)
            }
            if (locationDetails.country.isNotEmpty()) {
                if (isNotEmpty()) append(", ")
                append(locationDetails.country)
            }
        }
    }
    
    if (locationName.isNotEmpty()) {
        InfoTag(
            text = locationName,
            icon = Icons.Default.LocationOn
        )
    }
}