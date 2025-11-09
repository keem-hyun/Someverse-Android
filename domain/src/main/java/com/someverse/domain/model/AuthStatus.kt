package com.someverse.domain.model

/**
 * Auth Status Model
 * - Used after OAuth login to check onboarding status
 * - Maps to backend API response from GET /users/me
 * - Lightweight model for authentication state only
 *
 * Backend Response:
 * {
 *   "userId": 2,
 *   "email": "user@example.com",
 *   "realName": "홍길동",
 *   "provider": "KAKAO",
 *   "gender": "WOMAN",
 *   "birthDate": "1997-03-17T00:00:00.000+00:00",
 *   "phone": "01012345678",
 *   "onboardingCompleted": true
 * }
 */
data class AuthStatus(
    val userId: String,
    val email: String,
    val realName: String?,
    val provider: SocialProvider, // Enum for type safety
    val onboardingCompleted: Boolean
) {
    /**
     * Check if user needs to go through onboarding
     */
    val needsOnboarding: Boolean
        get() = !onboardingCompleted
}