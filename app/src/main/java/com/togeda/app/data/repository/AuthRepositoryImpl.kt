package com.togeda.app.data.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.togeda.app.data.model.generated.ApiError
import com.togeda.app.data.model.generated.RefreshTokenResponseDTO
import com.togeda.app.data.model.generated.SignUpResponseDto
import com.togeda.app.data.model.generated.SuccessDto
import com.togeda.app.data.model.generated.UserRegisterDto
import com.togeda.app.data.remote.generated.CognitoControllerApi
import com.togeda.app.data.security.TokenManager
import com.togeda.app.data.security.TokenRefreshManager
import com.togeda.app.domain.model.User
import com.togeda.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val cognitoApi: CognitoControllerApi,
    private val tokenManager: TokenManager,
    private val tokenRefreshManager: TokenRefreshManager
) : AuthRepository {
    
    private val currentUserFlow = MutableStateFlow<User?>(null)
    
    override suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = cognitoApi.login(email, password)
            
            // Store tokens securely
            tokenManager.storeTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                userId = response.userId,
                expiresIn = 3600
            )
            
            val user = User(
                id = response.userId,
                email = email,
                name = email.split("@").firstOrNull() ?: email,
                token = response.accessToken
            )
            currentUserFlow.value = user
            Result.success(user)
        } catch (e: Exception) {
            val userFriendlyMessage = when (e) {
                is org.openapitools.client.infrastructure.ClientException -> {
                    try {
                                val response = e.response
        if (response is org.openapitools.client.infrastructure.ClientError<*>) {
            val errorBody = response.body as? String
            if (!errorBody.isNullOrBlank()) {
                val objectMapper = ObjectMapper()
                val apiError = objectMapper.readValue(errorBody, ApiError::class.java)
                apiError.message ?: "Invalid email or password. Please check your credentials and try again."
            } else {
                "Invalid email or password. Please check your credentials and try again."
            }
        } else {
            "Invalid email or password. Please check your credentials and try again."
        }
    } catch (parseException: Exception) {
        "Invalid email or password. Please check your credentials and try again."
    }
                }
                is org.openapitools.client.infrastructure.ServerException -> {
                    "Server error. Please try again later."
                }
                is java.net.UnknownHostException -> {
                    "No internet connection. Please check your network and try again."
                }
                is java.net.SocketTimeoutException -> {
                    "Request timed out. Please check your internet connection and try again."
                }
                is java.net.ConnectException -> {
                    "Unable to connect to server. Please check your internet connection and try again."
                }
                else -> {
                    "Login failed. Please try again."
                }
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
    
    suspend fun signUp(
        email: String,
        password: String
    ): Result<SignUpResponseDto> = withContext(Dispatchers.IO) {
        try {
            val userRegisterDto = UserRegisterDto(
                email = email,
                password = password
            )
            val response = cognitoApi.signUp(userRegisterDto)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun confirmSignUp(userId: String, confirmationCode: String): Result<SuccessDto> = withContext(Dispatchers.IO) {
        try {
            val response = cognitoApi.confirmSignUp(userId, confirmationCode)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshToken(refreshToken: String, userId: String): Result<RefreshTokenResponseDTO> = withContext(Dispatchers.IO) {
        try {
            val response = cognitoApi.refreshToken(refreshToken, userId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentUserFromTokens(): User? {
        val userId = tokenManager.getUserId()
        val accessToken = tokenManager.getAccessToken()
        
        if (userId.isNullOrBlank() || accessToken.isNullOrBlank()) {
            return null
        }
        
        return User(
            id = userId,
            email = "",
            name = userId,
            token = accessToken
        )
    }
    
    suspend fun <T> executeWithTokenRefresh(apiCall: suspend () -> T): Result<T> {
        return tokenRefreshManager.executeWithTokenRefresh(apiCall)
    }
}
