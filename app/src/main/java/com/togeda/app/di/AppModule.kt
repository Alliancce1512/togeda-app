package com.togeda.app.di

import com.togeda.app.data.repository.AuthRepositoryImpl
import com.togeda.app.domain.repository.AuthRepository
import com.togeda.app.domain.usecase.LoginUseCase
import com.togeda.app.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    single { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
