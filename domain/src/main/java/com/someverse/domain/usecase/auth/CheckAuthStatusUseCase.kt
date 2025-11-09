package com.someverse.domain.usecase.auth

import com.someverse.domain.model.AuthStatus
import com.someverse.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Check Auth Status Use Case (After OAuth Login)
 *
 * Flow:
 * 1. WebView completes OAuth and extracts JWT token
 * 2. App saves JWT token locally
 * 3. This UseCase calls GET /users/me to check auth status
 * 4. Returns AuthStatus with onboardingCompleted flag
 * 5. UI decides whether to show onboarding or main screen
 *
 * Backend API:
 * - Method: GET
 * - URL: /users/me
 * - Headers: Authorization: Bearer {JWT}
 * - Response: AuthStatus with onboardingCompleted
 *
 * Usage:
 * ```
 * // After saving JWT token
 * val result = checkAuthStatusUseCase()
 * result.onSuccess { authStatus ->
 *     if (authStatus.onboardingCompleted) {
 *         // Navigate to main screen
 *     } else {
 *         // Navigate to onboarding
 *     }
 * }
 * ```
 */
class CheckAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Check authentication status and onboarding state
     *
     * @return Result<AuthStatus> containing auth status from backend
     */
    suspend operator fun invoke(): Result<AuthStatus> {
        return authRepository.getAuthStatus()
    }
}
