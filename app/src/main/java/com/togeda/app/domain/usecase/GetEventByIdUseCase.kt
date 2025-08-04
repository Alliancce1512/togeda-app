package com.togeda.app.domain.usecase

import com.togeda.app.domain.model.Event
import com.togeda.app.domain.repository.EventRepository

class GetEventByIdUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(eventId: String): Event {
        return eventRepository.getEventById(eventId)
    }
} 