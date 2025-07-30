package com.togeda.app.domain.model

data class Event(
    val id: String,
    val title: String,
    val organizer: String,
    val organizerAvatar: String?,
    val imageUrl: String?,
    val isFree: Boolean,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val currentAttendees: Int,
    val maxAttendees: Int,
    val location: String,
    val isPublic: Boolean,
    val locationConfirmed: Boolean
) 