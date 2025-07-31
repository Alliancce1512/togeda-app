package com.togeda.app.di

import com.togeda.app.data.location.LocationService
import com.togeda.app.data.remote.PostsApi
import com.togeda.app.data.remote.generated.CognitoControllerApi
import com.togeda.app.data.remote.generated.PostControllerApi
import com.togeda.app.data.remote.generated.UserControllerApi
import com.togeda.app.data.repository.AuthRepositoryImpl
import com.togeda.app.data.repository.EventRepositoryImpl
import com.togeda.app.data.security.TokenManager
import com.togeda.app.data.security.TokenRefreshManager
import com.togeda.app.domain.repository.AuthRepository
import com.togeda.app.domain.repository.EventRepository
import com.togeda.app.domain.usecase.GetEventsUseCase
import com.togeda.app.domain.usecase.LoginUseCase
import com.togeda.app.presentation.feed.FeedViewModel
import com.togeda.app.presentation.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    
    // Network dependencies
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor { chain ->
                val tokenManager    = get<TokenManager>()
                val accessToken     = tokenManager.getAccessToken()

                val request         = if (!accessToken.isNullOrBlank()) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build()
                } else {
                    chain.request()
                }

                chain.proceed(request)
            }
            .build()
    }
    
    // Retrofit for PostsApi
    single {
        Retrofit.Builder()
            .baseUrl("http://dev-interview-task-env.eba-yztmpypv.eu-central-1.elasticbeanstalk.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // API Clients
    single<CognitoControllerApi> { 
        CognitoControllerApi(
            basePath = "http://dev-interview-task-env.eba-yztmpypv.eu-central-1.elasticbeanstalk.com",
            client = get()
        )
    }
    
    single<PostControllerApi> { 
        PostControllerApi(
            basePath = "http://dev-interview-task-env.eba-yztmpypv.eu-central-1.elasticbeanstalk.com",
            client = get()
        )
    }
    
    single<UserControllerApi> { 
        UserControllerApi(
            basePath = "http://dev-interview-task-env.eba-yztmpypv.eu-central-1.elasticbeanstalk.com",
            client = get()
        )
    }
    
    single<PostsApi> {
        get<Retrofit>().create(PostsApi::class.java)
    }
    
    // Location Service
    single { LocationService() }

    // Token Management
    single { TokenManager(get()) }
    single { TokenRefreshManager(get(), get(), CoroutineScope(Dispatchers.IO)) }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<EventRepository> { EventRepositoryImpl(get(), get()) }

    // Use Cases
    single { LoginUseCase(get()) }
    single { GetEventsUseCase(get()) }
    
    // ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { FeedViewModel(get(), get()) }
}
