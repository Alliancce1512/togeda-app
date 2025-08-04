package com.togeda.app.data.repository

import com.togeda.app.data.location.LocationService
import com.togeda.app.data.mapper.PostMapper
import com.togeda.app.data.remote.PostsApi
import com.togeda.app.domain.model.Event
import com.togeda.app.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventRepositoryImpl(
    private val postsApi        : PostsApi,
    private val locationService : LocationService
) : EventRepository {
    private val eventsFlow              = MutableStateFlow<List<Event>>(emptyList())
    private var currentPage             = 0
    private var isLastPage              = false
    private var sessionId   : String?   = null
    
    override fun getEvents(): Flow<List<Event>> = eventsFlow.asStateFlow()
    
    override suspend fun refreshEvents() {
        try {
            currentPage = 0
            isLastPage  = false
            sessionId   = null
            
            val (latitude, longitude) = locationService.getCurrentLocation()
            
            val response = postsApi.getAllPosts(
                sortBy      = "CREATED_AT",
                longitude   = longitude,
                latitude    = latitude,
                distance    = 50,
                pageNumber  = 0,
                pageSize    = 20
            )

            eventsFlow.value    = response.data.map { PostMapper.mapToEvent(it) }
            currentPage         = 1
            isLastPage          = response.lastPage
            
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getEventById(eventId: String): Event {
        try {
            return PostMapper.mapToEvent(postsApi.getPostById(eventId))
        } catch (e: Exception) {
            throw e
        }
    }
} 