package com.someverse.domain.repository

import com.someverse.domain.model.AuthStatus
import com.someverse.domain.model.AuthToken
import com.someverse.domain.model.Gender
import com.someverse.domain.model.User
import com.someverse.domain.model.Location

/**
 * Authentication Repository Interface
 * - Handles authentication related operations
 * - Implementation will be in data layer
 * - Use Result<T> for error handling
 *
 * Note: OAuth login flow is handled entirely by backend.
 * After OAuth:
 * 1. Save JWT token
 * 2. Call getAuthStatus() to check onboarding status
 * 3. If onboarding complete, fetch full profile via UserRepository
 */
interface AuthRepository {

    // ==================== Authentication ====================

    /**
     * Get auth status after OAuth login
     * Calls GET /users/me to check if onboarding is completed
     *
     * @return AuthStatus with onboarding state
     */
    suspend fun getAuthStatus(): Result<AuthStatus>

    /**
     * Logout
     */
    suspend fun logout(): Result<Unit>

    /**
     * Refresh token
     */
    suspend fun refreshToken(refreshToken: String): Result<AuthToken>

    // ==================== Onboarding Steps ====================

    /**
     * Submit nickname (Step 1)
     * @return Updated User with nickname
     */
    suspend fun submitNickname(nickname: String): Result<User>

    /**
     * Submit gender (Step 2)
     * @return Updated User with gender
     */
    suspend fun submitGender(gender: Gender): Result<User>

    /**
     * Submit age (Step 3)
     * @return Updated User with age
     */
    suspend fun submitAge(age: Int): Result<User>

    /**
     * Submit address (Step 4)
     * @return Updated User with address
     */
    suspend fun submitAddress(address: List<String>): Result<User>

    /**
     * Submit profile image URL (Step 5)
     * @return Updated User with profile image
     *
     * Flow:
     * 1. File uploaded to S3 via FileRepository
     * 2. S3 returns URL (e.g., https://bucket.s3.region.amazonaws.com/profile/user123.jpg)
     * 3. This method sends URL to backend API
     * 4. Backend stores URL in database and returns updated User
     */
    suspend fun submitProfileImage(imageUrl: String): Result<User>

    /**
     * Get all available locations for address selection
     * @return List of available locations (city + district)
     */
    suspend fun getAddressList(): Result<List<Location>>
}