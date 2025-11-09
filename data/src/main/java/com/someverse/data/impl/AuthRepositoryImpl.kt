package com.someverse.data.impl

import com.someverse.data.source.AuthDataSource
import com.someverse.domain.model.AuthStatus
import com.someverse.domain.model.AuthToken
import com.someverse.domain.model.Gender
import com.someverse.domain.model.SocialProvider
import com.someverse.domain.model.User
import com.someverse.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * AuthRepository Implementation
 * - Depends on AuthDataSource interface (not concrete implementation)
 * - Can use AuthLocalDataSource or AuthRemoteDataSource
 * - Handles business logic and data transformation
 * - Converts Entity ↔ Domain models
 *
 * Note: OAuth login is handled by backend.
 * Use UserRepository.getCurrentUser() to fetch user after OAuth.
 */
class AuthRepositoryImpl @Inject constructor(
    private val dataSource: AuthDataSource  // Interface, not concrete class!
) : AuthRepository {

    // ==================== Authentication ====================

    override suspend fun getAuthStatus(): Result<AuthStatus> {
        return try {
            // Call dataSource to get auth status from API
            val (userEntity, onboardingCompleted) = dataSource.getAuthStatus()

            // Validate required fields
            if (userEntity.email.isNullOrBlank()) {
                return Result.failure(IllegalStateException("Email is required in auth status"))
            }

            // Map UserEntity to AuthStatus (lightweight model)
            val authStatus = AuthStatus(
                userId = userEntity.id,
                email = userEntity.email,
                realName = userEntity.realName,
                provider = SocialProvider.fromString(userEntity.provider),
                onboardingCompleted = onboardingCompleted
            )

            Result.success(authStatus)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            dataSource.logout()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthToken> {
        return try {
            val tokenEntity = dataSource.refreshToken(refreshToken)
            Result.success(tokenEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== Onboarding Steps ====================

    override suspend fun submitNickname(nickname: String): Result<User> {
        return try {
            // Validate nickname (business logic)
            if (nickname.isBlank()) {
                return Result.failure(Exception("Nickname cannot be empty"))
            }

            // Delegate to dataSource and get updated user
            val userEntity = dataSource.submitNickname(nickname)
            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitGender(gender: Gender): Result<User> {
        return try {
            val userEntity = dataSource.submitGender(gender)
            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitAge(age: Int): Result<User> {
        return try {
            // Validate age (business logic)
            if (age < 0 || age > 150) {
                return Result.failure(Exception("Invalid age"))
            }

            val userEntity = dataSource.submitAge(age)
            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitAddress(address: String): Result<User> {
        return try {
            if (address.isBlank()) {
                return Result.failure(Exception("Address cannot be empty"))
            }

            val userEntity = dataSource.submitAddress(address)
            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitProfileImage(imageUrl: String): Result<User> {
        return try {
            val userEntity = dataSource.submitProfileImage(imageUrl)
            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}