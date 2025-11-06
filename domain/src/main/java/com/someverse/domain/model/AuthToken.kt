package com.someverse.domain.model

/**
 * Authentication Token
 * - Contains access token and refresh token
 * - Pure Kotlin data class
 */
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val tokenType: String = "Bearer"
)
