package com.togeda.app.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Secure token manager for storing and managing authentication tokens.
 * Uses EncryptedSharedPreferences for secure storage.
 */
class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_tokens",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

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
        encryptedSharedPreferences.edit()
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
        return encryptedSharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    /**
     * Get the stored refresh token
     */
    fun getRefreshToken(): String? {
        return encryptedSharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    /**
     * Get the stored user ID
     */
    fun getUserId(): String? {
        return encryptedSharedPreferences.getString(KEY_USER_ID, null)
    }

    /**
     * Check if tokens are valid (not expired)
     */
    fun isTokenValid(): Boolean {
        val expiryTime = encryptedSharedPreferences.getLong(KEY_TOKEN_EXPIRY, 0L)
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
        encryptedSharedPreferences.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_USER_ID)
            .remove(KEY_TOKEN_EXPIRY)
            .apply()

        _isLoggedIn.value = false
    }
} 