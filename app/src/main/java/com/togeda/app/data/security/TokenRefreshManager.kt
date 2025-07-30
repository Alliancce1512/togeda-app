package com.togeda.app.data.security


import com.togeda.app.data.remote.generated.CognitoControllerApi
import com.togeda.app.data.model.generated.RefreshTokenResponseDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Manages automatic token refresh and provides authenticated API calls.
 */
class TokenRefreshManager(
    private val tokenManager: TokenManager,
    private val cognitoApi: CognitoControllerApi,
    private val coroutineScope: CoroutineScope
) {



    /**
     * Execute an API call with automatic token refresh if needed.
     * This ensures the user always has a valid token for API calls.
     */
    suspend fun <T> executeWithTokenRefresh(apiCall: suspend () -> T): Result<T> {
        return try {
            // Check if we need to refresh the token
            if (tokenManager.isTokenExpiringSoon()) {
                refreshToken()
            }

            // Execute the API call
            val result = apiCall()
            Result.success(result)
        } catch (e: Exception) {
            if (isAuthenticationError(e)) {
                try {
                    refreshToken()
                    val retryResult = apiCall()
                    Result.success(retryResult)
                } catch (retryException: Exception) {
                    Result.failure(retryException)
                }
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Refresh the access token using the refresh token
     */
    private suspend fun refreshToken(): Result<RefreshTokenResponseDTO> {
        return withContext(Dispatchers.IO) {
            try {
                val refreshToken = tokenManager.getRefreshToken()
                val userId = tokenManager.getUserId()

                if (refreshToken.isNullOrBlank() || userId.isNullOrBlank()) {
                    return@withContext Result.failure(Exception("No refresh token available"))
                }

                val response = cognitoApi.refreshToken(refreshToken, userId)
                
                // Update the stored tokens
                tokenManager.updateAccessToken(response.accessToken)
                
                Result.success(response)
            } catch (e: Exception) {
                // If refresh fails, clear tokens and force re-login
                if (isAuthenticationError(e)) {
                    tokenManager.clearTokens()
                }
                
                Result.failure(e)
            }
        }
    }

    /**
     * Check if an exception is an authentication error
     */
    private fun isAuthenticationError(exception: Exception): Boolean {
        return when (exception) {
            is org.openapitools.client.infrastructure.ClientException -> {
                exception.statusCode == 401 || exception.statusCode == 403
            }
            is org.openapitools.client.infrastructure.ServerException -> {
                exception.statusCode == 401 || exception.statusCode == 403
            }
            else -> false
        }
    }

    /**
     * Get the current access token, refreshing if necessary
     */
    suspend fun getValidAccessToken(): String? {
        if (tokenManager.isTokenExpiringSoon()) {
            refreshToken()
        }
        return tokenManager.getAccessToken()
    }

    /**
     * Check if user is currently logged in
     */
    suspend fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn.first()
    }

    /**
     * Logout user by clearing tokens
     */
    fun logout() {
        tokenManager.clearTokens()
    }
} 