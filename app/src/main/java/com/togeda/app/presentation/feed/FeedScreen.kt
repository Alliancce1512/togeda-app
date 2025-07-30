package com.togeda.app.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.togeda.app.presentation.common.EventCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val colorScheme = colorScheme

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            TopBar(
                onFilterClick = { },
                onSearchClick = { },
                onNotificationClick = { },
                onLogoutClick = { viewModel.onLogoutClick() }
            )
            
            // Tab Navigation
            TabNavigation(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.onTabSelected(it) }
            )
            
            // Content based on selected tab
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (state.selectedTab) {
                    FeedTab.EVENTS -> EventsContent(
                        events = state.events,
                        isLoading = state.isLoading,
                        onRefresh = { viewModel.onRefresh() }
                    )
                    FeedTab.CLUBS -> ClubsContent()
                    FeedTab.FRIENDS -> FriendsContent()
                }
            }
        }
        
        // Bottom Navigation - positioned at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            BottomNavigation()
        }
        
        // Logout Confirmation Dialog
        val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onLogoutCancel() },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onLogoutConfirm() }
                    ) {
                        Text("Yes, Logout")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.onLogoutCancel() }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun TopBar(
    onFilterClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App title
        Text(
            text = "Togeda",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        
        // Action icons
        Row(
            verticalAlignment       = Alignment.CenterVertically,
            horizontalArrangement   = Arrangement.spacedBy(8.dp)
        ) {
            TopNavButton(
                onClick = onFilterClick,
                icon    = Icons.Default.Tune
            )

            TopNavButton(
                onClick = onSearchClick,
                icon    = Icons.Default.Search
            )

            TopNavButton(
                onClick = onNotificationClick,
                icon    = Icons.Default.Notifications
            )

            TopNavButton(
                onClick = onLogoutClick,
                icon    = Icons.AutoMirrored.Filled.Logout,
                backgroundColor = colorScheme.errorContainer,
                iconColor = colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun TopNavButton(
    onClick         : () -> Unit,
    backgroundColor : Color = colorScheme.surface,
    iconColor       : Color = colorScheme.onBackground,
    icon            : ImageVector,
    modifier        : Modifier = Modifier
) {
    IconButton(
        modifier    = modifier
            .size(32.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .padding(all = 6.dp),
        onClick     = onClick
    ) {
        Icon(
            imageVector         = icon,
            contentDescription  = null,
            tint                = iconColor
        )
    }
}

@Composable
private fun TabNavigation(
    selectedTab: FeedTab,
    onTabSelected: (FeedTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FeedTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Button(
                onClick = { onTabSelected(tab) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) colorScheme.primary else colorScheme.surface,
                    contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface
                )
            ) {
                Text(
                    text = tab.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EventsContent(
    events: List<com.togeda.app.domain.model.Event>,
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    onEventClick = { /* TODO: Navigate to event details */ },
                    onBookmarkClick = { /* TODO: Bookmark event */ },
                    onMoreClick = { /* TODO: Show more options */ }
                )
            }
        }
    }
}

@Composable
private fun ClubsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Clubs content coming soon",
            color = colorScheme.onBackground
        )
    }
}

@Composable
private fun FriendsContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Friends content coming soon",
            color = colorScheme.onBackground
        )
    }
}

@Composable
private fun BottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home (selected)
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Map
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "Map",
                tint = Color(0xFF444444),
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Add/Create
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color(0xFF444444),
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Chat
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = "Chat",
                tint = Color(0xFF444444),
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Profile
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = colorScheme.surface,
                modifier = Modifier.size(24.dp)
            )
        }
    }
} 