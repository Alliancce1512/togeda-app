package com.togeda.app.domain.model

data class Event(
    val id                  : String,
    val title               : String,
    val description         : String?,
    val organizer           : String,
    val organizerAvatar     : String?,
    val images              : List<String>,
    val isFree              : Boolean,
    val startDate           : String,
    val endDate             : String,
    val startTime           : String,
    val endTime             : String,
    val currentAttendees    : Int,
    val maxAttendees        : Int,
    val location            : String,
    val locationDetails     : LocationDetails?,
    val isPublic            : Boolean,
    val locationConfirmed   : Boolean,
    val payment             : Double,
    val currency            : String?,
    val status              : String,
    val currentUserStatus   : String,
    val savedByCurrentUser  : Boolean,
    val rating              : Double?,
    val interests           : List<String>
)

data class LocationDetails(
    val name        : String,
    val address     : String,
    val city        : String,
    val state       : String,
    val country     : String,
    val latitude    : Double,
    val longitude   : Double
) 