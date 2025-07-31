package com.togeda.app.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.togeda.app.domain.repository.AuthRepository
import com.togeda.app.domain.usecase.GetEventsUseCase
import com.togeda.app.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getEventsUseCase    : GetEventsUseCase,
    private val authRepository      : AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state: StateFlow<FeedState> = _state.asStateFlow()
    
    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog.asStateFlow()

    init {
        loadEvents()
    }

    fun onTabSelected(tab: FeedTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onRefresh() {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, eventsState = UiState.Loading) }
            
            try {
                getEventsUseCase.refreshEvents()
                getEventsUseCase().collect { events ->
                    _state.update { 
                        it.copy(
                            events      = events,
                            eventsState = UiState.Success(events),
                            isLoading   = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        eventsState = UiState.Error(e.message ?: "Failed to load events"),
                        isLoading   = false
                    )
                }
            }
        }
    }

    fun onLogoutClick() {
        _showLogoutDialog.value = true
    }
    
    fun onLogoutConfirm() {
        viewModelScope.launch {
            authRepository.logout()
            _showLogoutDialog.value = false
        }
    }
    
    fun onLogoutCancel() {
        _showLogoutDialog.value = false
    }
} 