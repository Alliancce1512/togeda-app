package com.togeda.app.data.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.togeda.app.data.model.generated.ApiError
import com.togeda.app.data.remote.generated.CognitoControllerApi
import com.togeda.app.data.security.TokenManager
import com.togeda.app.domain.model.User
import com.togeda.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.openapitools.client.infrastructure.ClientError
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AuthRepositoryImpl(
    private val cognitoApi          : CognitoControllerApi,
    private val tokenManager        : TokenManager
) : AuthRepository {
    
    private val currentUserFlow = MutableStateFlow<User?>(null)
    
    override suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = cognitoApi.login(email, password)
            
            // Store tokens securely
            tokenManager.storeTokens(
                accessToken     = response.accessToken,
                refreshToken    = response.refreshToken,
                userId          = response.userId,
                expiresIn       = 3600 // mock time, subject to change
            )
            
            val user = User(
                id      = response.userId,
                email   = email,
                name    = email.split("@").firstOrNull() ?: email,
                token   = response.accessToken
            )
            currentUserFlow.value = user

            Result.success(user)
        } catch (e: Exception) {
            val userFriendlyMessage = when (e) {
                is ClientException          -> {
                    try {
                        val response = e.response

                        if (response is ClientError<*>) {
                            val errorBody = response.body as? String

                            if (!errorBody.isNullOrBlank()) {
                                val objectMapper    = ObjectMapper()
                                val apiError        = objectMapper.readValue(errorBody, ApiError::class.java)

                                apiError.message ?: "Invalid email or password. Please check your credentials and try again."
                            } else {
                                "Invalid email or password. Please check your credentials and try again."
                            }
                        } else {
                            "Invalid email or password. Please check your credentials and try again."
                        }
                    } catch (_: Exception) {
                        "Invalid email or password. Please check your credentials and try again."
                    }
                }
                is ServerException          -> "Server error. Please try again later."
                is UnknownHostException     -> "No internet connection. Please check your network and try again."
                is SocketTimeoutException   -> "Request timed out. Please check your internet connection and try again."
                is ConnectException         -> "Unable to connect to server. Please check your internet connection and try again."
                else                        -> "Login failed. Please try again."
            }
            Result.failure(Exception(userFriendlyMessage))
        }
    }
    
    override suspend fun logout() {
        // Clear stored tokens
        tokenManager.clearTokens()
        currentUserFlow.value = null
    }
    
    override fun getCurrentUser(): Flow<User?> = currentUserFlow
    
    override fun isLoggedIn(): Flow<Boolean> = tokenManager.isLoggedIn
}
