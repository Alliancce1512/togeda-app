package com.togeda.app.domain.usecase

import com.togeda.app.domain.model.Event
import com.togeda.app.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class GetEventsUseCase(
    private val eventRepository: EventRepository
) {
    operator fun invoke(): Flow<List<Event>> = eventRepository.getEvents()
    
    suspend fun refreshEvents() {
        eventRepository.refreshEvents()
    }
} 