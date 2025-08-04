package com.togeda.app.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.Star
import com.togeda.app.domain.model.Event

fun generateEventTags(event: Event): List<EventTag> {
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