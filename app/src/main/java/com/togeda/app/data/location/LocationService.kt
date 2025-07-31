package com.togeda.app.data.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationService() {
    
    suspend fun getCurrentLocation(): Pair<Double, Double> = withContext(Dispatchers.IO) {
        // For now, return Sofia coordinates as default
        return@withContext Pair(42.6977, 23.3219) // Sofia, Bulgaria
    }
} 