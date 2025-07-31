package com.togeda.app.presentation.feed

import com.togeda.app.domain.model.Event
import com.togeda.app.presentation.common.UiState

data class FeedState(
    val events      : List<Event> = emptyList(),
    val selectedTab : FeedTab = FeedTab.EVENTS,
    val eventsState : UiState<List<Event>> = UiState.Success(emptyList()),
    val isLoading   : Boolean = false
)

enum class FeedTab {
    EVENTS, CLUBS, FRIENDS
} 