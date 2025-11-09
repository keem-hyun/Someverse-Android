package com.someverse.presentation.ui.auth.login

import com.someverse.domain.model.SocialProvider

/**
 * Login UI State
 * - Represents the state of login screen after OAuth
 * - Contains minimal auth info for navigation decision
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val userId: String? = null,
    val email: String? = null,
    val provider: SocialProvider? = null,
    val isLoginSuccess: Boolean = false,
    val needsOnboarding: Boolean = false,
    val error: String? = null
)
