package com.someverse.domain.model

/**
 * User Model
 * - nickname, gender, birth, address may be null during onboarding
 * - Use isOnboardingComplete to check if profile is fully set up
 */
data class User(
    val id: String,
    val email: String,
    val nickname: String?,
    val gender: Gender?,
    val birth: String?,
    val address: List<String>?,
    val profileImageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
) {
    /**
     * Check if onboarding is complete
     * All required fields must be non-null
     */
    val isOnboardingComplete: Boolean
        get() = nickname != null &&
                gender != null &&
                birth != null &&
                address != null &&
                profileImageUrl != null
}