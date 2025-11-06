package com.someverse.domain.repository

import com.someverse.domain.model.User

/**
 * Repository Interface
 * - Define WHAT operations are available
 * - Implementation will be in data layer
 * - Use Result<T> for error handling
 */
interface UserRepository {

    suspend fun getUser(userId: String): Result<User>

    suspend fun updateUser(user: User): Result<Unit>

    suspend fun deleteUser(userId: String): Result<Unit>
}
