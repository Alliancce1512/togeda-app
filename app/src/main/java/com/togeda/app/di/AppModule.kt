package com.togeda.app.di

import com.togeda.app.data.repository.AuthRepositoryImpl
import com.togeda.app.data.repository.EventRepositoryImpl
import com.togeda.app.domain.repository.AuthRepository
import com.togeda.app.domain.repository.EventRepository
import com.togeda.app.domain.usecase.GetEventsUseCase
import com.togeda.app.domain.usecase.LoginUseCase
import com.togeda.app.presentation.feed.FeedViewModel
import com.togeda.app.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<AuthRepository> { AuthRepositoryImpl() }
    single<EventRepository> { EventRepositoryImpl() }
    
    // Use Cases
    single { LoginUseCase(get()) }
    single { GetEventsUseCase(get()) }
    
    // ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { FeedViewModel(get()) }
}
