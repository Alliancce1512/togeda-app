package com.togeda.app.data.repository

import com.togeda.app.domain.model.User
import com.togeda.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthRepositoryImpl : AuthRepository {
    private val currentUserFlow = MutableStateFlow<User?>(null)
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val user = User(
                id = "1",
                email = email,
                name = "John Doe",
                token = "mock_token_123"
            )
            currentUserFlow.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun logout() {
        currentUserFlow.value = null
    }
    override fun getCurrentUser(): Flow<User?> = currentUserFlow
    override fun isLoggedIn(): Flow<Boolean> = MutableStateFlow(currentUserFlow.value != null)
}
