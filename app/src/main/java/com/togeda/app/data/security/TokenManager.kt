package com.togeda.app.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Secure token manager for storing and managing authentication tokens.
 * Uses EncryptedSharedPreferences for secure storage with fallback to regular SharedPreferences.
 */
class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences = try {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "secure_tokens",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (_: Exception) {
        // Fallback to regular SharedPreferences if EncryptedSharedPreferences fails
        // This can happen due to keystore corruption or device issues
        context.getSharedPreferences("secure_tokens_fallback", Context.MODE_PRIVATE)
    }

    private val _isLoggedIn = MutableStateFlow(hasValidTokens())
    val isLoggedIn: Flow<Boolean> = _isLoggedIn.asStateFlow()

    companion object {
        private const val KEY_ACCESS_TOKEN  = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID       = "user_id"
        private const val KEY_TOKEN_EXPIRY  = "token_expiry"
    }

    /**
     * Store authentication tokens securely
     */
    fun storeTokens(
        accessToken     : String,
        refreshToken    : String,
        userId          : String,
        expiresIn       : Long? = null
    ) {
        sharedPreferences.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putString(KEY_USER_ID, userId)
            .apply {
                if (expiresIn != null) {
                    putLong(KEY_TOKEN_EXPIRY, System.currentTimeMillis() + (expiresIn * 1000))
                }
            }
            .apply()

        _isLoggedIn.value = true
    }

    /**
     * Get the stored access token
     */
    fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    /**
     * Get the stored refresh token
     */
    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    /**
     * Get the stored user ID
     */
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    /**
     * Check if tokens are valid (not expired)
     */
    fun isTokenValid(): Boolean {
        val expiryTime = sharedPreferences.getLong(KEY_TOKEN_EXPIRY, 0L)
        return expiryTime == 0L || System.currentTimeMillis() < expiryTime
    }

    /**
     * Check if we have valid tokens stored
     */
    fun hasValidTokens(): Boolean {
        val accessToken = getAccessToken()
        val refreshToken = getRefreshToken()
        val userId = getUserId()
        
        return !accessToken.isNullOrBlank() && 
               !refreshToken.isNullOrBlank() && 
               !userId.isNullOrBlank() &&
               isTokenValid()
    }

    /**
     * Clear all stored tokens (logout)
     */
    fun clearTokens() {
        sharedPreferences.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_USER_ID)
            .remove(KEY_TOKEN_EXPIRY)
            .apply()

        _isLoggedIn.value = false
    }
} 