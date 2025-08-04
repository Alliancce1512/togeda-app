package com.togeda.app.data.mapper

import com.togeda.app.data.remote.PostResponseDto
import com.togeda.app.domain.model.Event
import com.togeda.app.domain.model.Interest
import com.togeda.app.domain.model.LocationDetails
import java.text.SimpleDateFormat
import java.util.Locale

object PostMapper {
    
    fun mapToEvent(post: PostResponseDto): Event {
        val dateFormat          = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val fromDate            = try {
            post.fromDate?.let { dateFormat.parse(it) }
        } catch (_: Exception) {
            null
        }
        
        val toDate              = try {
            post.toDate?.let { dateFormat.parse(it) }
        } catch (_: Exception) {
            null
        }

        val displayDateFormat   = SimpleDateFormat("dd.MM", Locale.getDefault())
        val timeFormat          = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        val startDate           = fromDate?.let { displayDateFormat.format(it) } ?: "TBD"
        val endDate             = toDate?.let { displayDateFormat.format(it) } ?: "TBD"
        val startTime           = fromDate?.let { timeFormat.format(it) } ?: "TBD"
        val endTime             = toDate?.let { timeFormat.format(it) } ?: "TBD"

        val locationString      = buildString {
            append(post.location?.city ?: "")
            if (!post.location?.state.isNullOrEmpty()) {
                append(", ${post.location.state}")
            }
            if (!post.location?.country.isNullOrEmpty()) {
                append(", ${post.location.country}")
            }
        }.ifEmpty { "Location TBD" }

        val locationDetails = post.location?.let { location ->
            LocationDetails(
                name        = location.name ?: "",
                address     = location.address ?: "",
                city        = location.city ?: "",
                state       = location.state ?: "",
                country     = location.country ?: "",
                latitude    = location.latitude ?: 0.0,
                longitude   = location.longitude ?: 0.0
            )
        }
        val interests   = post.interests?.map { interest ->
            Interest(
                name = interest.name ?: "",
                icon = interest.icon
            )
        }?.filter { it.name.isNotEmpty() } ?: emptyList()

        val payment     = post.payment ?: 0.0
        val isFree      = payment <= 0.0

        val firstName   = post.owner?.firstName ?: ""
        val lastName    = post.owner?.lastName ?: ""
        val organizer   = if (firstName.isNotEmpty() || lastName.isNotEmpty()) {
            "$firstName $lastName".trim()
        } else {
            "Unknown Organizer"
        }

        val organizerAvatar             = post.owner?.profilePhotos?.firstOrNull()
        val organizerOccupation         = post.owner?.occupation
        val images                      = post.images ?: emptyList()
        val maxAttendees                = post.maximumPeople ?: 0
        val currentAttendees            = post.participantsCount ?: 0
        val status                      = post.status ?: "UNKNOWN"
        val currentUserStatus           = post.currentUserStatus ?: "NOT_PARTICIPATING"
        val accessibility               = post.accessibility ?: "PUBLIC"
        val needsLocationalConfirmation = post.needsLocationalConfirmation ?: true
        val savedByCurrentUser          = post.savedByCurrentUser ?: false
        val rating                      = post.rating
        val currencySymbol              = post.currency?.symbol
        val askToJoin                   = post.askToJoin ?: false
        val blockedForCurrentUser       = post.blockedForCurrentUser ?: false
        
        return Event(
            id                      = post.id ?: "",
            title                   = post.title ?: "Untitled Event",
            description             = post.description,
            organizer               = organizer,
            organizerOccupation     = organizerOccupation,
            organizerAvatar         = organizerAvatar,
            images                  = images,
            isFree                  = isFree,
            startDate               = startDate,
            endDate                 = endDate,
            startTime               = startTime,
            endTime                 = endTime,
            currentAttendees        = currentAttendees,
            maxAttendees            = maxAttendees,
            location                = locationString,
            locationDetails         = locationDetails,
            isPublic                = accessibility == "PUBLIC",
            locationConfirmed       = !needsLocationalConfirmation,
            payment                 = payment,
            currency                = currencySymbol,
            status                  = status,
            currentUserStatus       = currentUserStatus,
            savedByCurrentUser      = savedByCurrentUser,
            rating                  = rating,
            interests               = interests,
            askToJoin               = askToJoin,
            blockedForCurrentUser   = blockedForCurrentUser
        )
    }
} 