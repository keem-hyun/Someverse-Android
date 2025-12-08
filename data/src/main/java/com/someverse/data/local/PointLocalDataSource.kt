package com.someverse.data.local

import com.someverse.data.source.PointDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Point Local DataSource (Mock Implementation)
 * - Implements PointDataSource interface
 * - Handles local/mock data for development
 * - Simulates API delays
 * - Can be replaced with Room database later for offline support
 */
@Singleton
class PointLocalDataSource @Inject constructor() : PointDataSource {

    // Mock point balance
    private var pointBalance = 3L

    override suspend fun getPointBalance(): Long {
        delay(300) // Simulate network delay
        println("💰 Local: Fetched point balance: $pointBalance")
        return pointBalance
    }
}