package com.someverse.data.source

import com.someverse.data.model.AuthTokenEntity
import com.someverse.data.model.UserEntity
import com.someverse.domain.model.Gender
import com.someverse.domain.model.Location

/**
 * Auth DataSource Interface
 * - Abstract data access operations
 * - Can be implemented by Local (mock) or Remote (API)
 * - Repository depends on this interface, not concrete implementations
 *
 * Note: OAuth is handled by backend.
 * Token saving is handled separately (TODO: TokenDataSource)
 */
interface AuthDataSource {

    // ==================== Authentication ====================

    /**
     * Get auth status (calls GET /users/me)
     * Returns basic auth info with onboarding status
     */
    suspend fun getAuthStatus(): Pair<UserEntity, Boolean> // (UserEntity, onboardingCompleted)

    /**
     * Perform logout
     */
    suspend fun logout()

    /**
     * Refresh authentication token
     */
    suspend fun refreshToken(refreshToken: String): AuthTokenEntity

    /**
     * Check if user is authenticated
     */
    suspend fun isAuthenticated(): Boolean

    /**
     * Clear all authentication data
     */
    suspend fun clearAuth()

    /**
     * Get stored token
     */
    suspend fun getToken(): AuthTokenEntity?

    /**
     * Save token to storage
     */
    suspend fun saveToken(token: AuthTokenEntity)

    // ==================== Onboarding Data ====================

    /**
     * Submit nickname
     * @return Updated UserEntity
     */
    suspend fun submitNickname(nickname: String): UserEntity

    /**
     * Submit gender
     * @return Updated UserEntity
     */
    suspend fun submitGender(gender: Gender): UserEntity

    /**
     * Submit age
     * @return Updated UserEntity
     */
    suspend fun submitAge(age: Int): UserEntity

    /**
     * Submit address
     * @return Updated UserEntity
     */
    suspend fun submitAddress(address: List<String>): UserEntity

    /**
     * Submit profile image URL
     * @return Updated UserEntity
     */
    suspend fun submitProfileImage(imageUrl: String): UserEntity

    // ==================== User Management ====================

    /**
     * Get current user
     */
    suspend fun getCurrentUser(): UserEntity?

    /**
     * Save user to local storage (cache)
     */
    suspend fun saveUser(user: UserEntity)

    /**
     * Get all available locations for address selection
     */
    suspend fun getAddressList(): List<Location>
}