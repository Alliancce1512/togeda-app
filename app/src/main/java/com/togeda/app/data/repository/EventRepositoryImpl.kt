package com.togeda.app.data.repository

import com.togeda.app.domain.model.Event
import com.togeda.app.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class EventRepositoryImpl : EventRepository {
    private val eventsFlow = MutableStateFlow<List<Event>>(emptyList())
    
    init {
        // Mock data without network images to avoid permission issues
        val mockEvents = listOf(
            Event(
                id = "1",
                title = "Два дни в Милано",
                organizer = "Nikolay Atanasov",
                organizerAvatar = null,
                imageUrl = null,
                isFree = true,
                startDate = "16.02",
                endDate = "17.02",
                startTime = "10:17",
                endTime = "21:27",
                currentAttendees = 2,
                maxAttendees = 50,
                location = "Sofiya, София-град, Бълга...",
                isPublic = true,
                locationConfirmed = false
            ),
            Event(
                id = "2",
                title = "Weekend in Paris",
                organizer = "Maria Petrova",
                organizerAvatar = null,
                imageUrl = null,
                isFree = false,
                startDate = "20.02",
                endDate = "22.02",
                startTime = "09:00",
                endTime = "18:00",
                currentAttendees = 15,
                maxAttendees = 30,
                location = "Paris, France",
                isPublic = true,
                locationConfirmed = true
            )
        )
        eventsFlow.value = mockEvents
    }
    
    override fun getEvents(): Flow<List<Event>> = eventsFlow
    
    override suspend fun refreshEvents() {
        // Mock data refresh - no actual API call
    }
} 