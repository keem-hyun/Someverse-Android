package com.someverse.domain.usecase.auth

import com.someverse.domain.model.AuthStatus
import com.someverse.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Social Login Use Case
 *
 * Complete OAuth login flow:
 * 1. Receive JWT token from OAuth callback
 * 2. Save JWT token to local storage (TODO: implement token storage)
 * 3. Fetch auth status to check onboarding state
 * 4. Return AuthStatus for navigation decision
 *
 * This UseCase encapsulates the entire post-OAuth flow,
 * maintaining Clean Architecture by keeping all business logic in domain layer.
 *
 * Backend Flow (already completed):
 * - WebView → Backend OAuth URL
 * - Backend → Social provider login
 * - User approves → Backend receives auth code
 * - Backend → Exchanges code for token, gets user info, creates JWT
 * - Backend → Redirects to app with JWT
 *
 * App Flow (this UseCase):
 * - WebView extracts JWT token
 * - **This UseCase: Save token + Get auth status**
 * - Return AuthStatus with onboarding state
 *
 * Usage:
 * ```
 * val result = socialLoginUseCase(jwtToken)
 * result.onSuccess { authStatus ->
 *     if (authStatus.onboardingCompleted) {
 *         navigateToMain()
 *     } else {
 *         navigateToOnboarding()
 *     }
 * }
 * ```
 */
class SocialLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
    // TODO: Inject TokenRepository for token storage
) {
    /**
     * Complete social login process
     *
     * @param jwtToken JWT token from OAuth callback
     * @return Result<AuthStatus> with user info and onboarding state
     */
    suspend operator fun invoke(jwtToken: String): Result<AuthStatus> {
        // Validate input
        if (jwtToken.isBlank()) {
            return Result.failure(
                IllegalArgumentException("JWT token cannot be blank")
            )
        }

        return try {
            // TODO: Save JWT token
            // tokenRepository.saveToken(jwtToken)

            // Get auth status (calls GET /users/me)
            authRepository.getAuthStatus()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
