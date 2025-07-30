package com.togeda.app.domain.repository

import com.togeda.app.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    suspend fun refreshEvents()
} 