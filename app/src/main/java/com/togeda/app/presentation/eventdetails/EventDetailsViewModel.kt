package com.togeda.app.presentation.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.togeda.app.domain.model.Event
import com.togeda.app.domain.usecase.GetEventByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EventDetailsState(
    val event           : Event? = null,
    val isLoading       : Boolean = false,
    val error           : String? = null,
)

sealed class EventDetailsEvent {
    data class LoadEvent(val eventId: String)   : EventDetailsEvent()
    object JoinEvent                            : EventDetailsEvent()
    object ShareEvent                           : EventDetailsEvent()
    object ToggleBookmark                       : EventDetailsEvent()
}

class EventDetailsViewModel(
    private val getEventByIdUseCase: GetEventByIdUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(EventDetailsState())
    val state: StateFlow<EventDetailsState> = _state.asStateFlow()

    fun onEvent(event: EventDetailsEvent) {
        when (event) {
            is EventDetailsEvent.LoadEvent -> {
                loadEvent(event.eventId)
            }
            EventDetailsEvent.JoinEvent -> {
                joinEvent()
            }
            EventDetailsEvent.ShareEvent -> {
                shareEvent()
            }
            EventDetailsEvent.ToggleBookmark -> {
                toggleBookmark()
            }
        }
    }

    private fun loadEvent(eventId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            try {
                _state.value = _state.value.copy(
                    event       = getEventByIdUseCase(eventId),
                    isLoading   = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error       = e.message ?: "Failed to load event",
                    isLoading   = false
                )
            }
        }
    }

    private fun joinEvent() { } // For later implementation

    private fun shareEvent() { } // For later implementation

    private fun toggleBookmark() { } // For later implementation
} 