package com.someverse.data.source

/**
 * Point DataSource Interface
 * - Abstract data access operations for point (루미) functionality
 * - Can be implemented by Local (mock) or Remote (API)
 * - Repository depends on this interface, not concrete implementations
 */
interface PointDataSource {

    /**
     * Get current user's point balance
     * @return Point balance (루미 개수)
     */
    suspend fun getPointBalance(): Long
}