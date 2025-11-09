package com.someverse.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.someverse.domain.usecase.auth.SocialLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.onSuccess

/**
 * Login ViewModel
 * - Manages login UI state
 * - Handles OAuth callback flow
 * - No business logic (delegated to UseCase)
 *
 * OAuth Flow:
 * 1. WebView completes OAuth → extracts JWT token
 * 2. LoginScreen calls login(jwt)
 * 3. ViewModel calls SocialLoginUseCase
 * 4. UseCase saves token + gets auth status
 * 5. ViewModel updates UI state based on result
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialLoginUseCase: SocialLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Handle OAuth login with JWT token
     *
     * @param jwtToken JWT token received from backend OAuth callback
     */
    fun login(jwtToken: String) {
        viewModelScope.launch {
            // Set loading state
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Call UseCase (handles token saving + auth status check)
            val result = socialLoginUseCase(jwtToken)

            // Update UI state based on result
            result.onSuccess { authStatus ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userId = authStatus.userId,
                        email = authStatus.email,
                        provider = authStatus.provider,
                        isLoginSuccess = true,
                        needsOnboarding = authStatus.needsOnboarding,
                        error = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Login failed",
                        isLoginSuccess = false
                    )
                }
            }
        }
    }

    /**
     * Mock login using the AuthLocalDataSource
     * Use this for testing the flow without an actual backend
     */
    fun mockLogin() {
        viewModelScope.launch {
            // Set loading state
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Use a mock JWT token to trigger the login flow
            val mockJwtToken = "mock_jwt_token_${System.currentTimeMillis()}"
            val result = socialLoginUseCase(mockJwtToken)

            // Update UI state based on result
            result.onSuccess { authStatus ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userId = authStatus.userId,
                        email = authStatus.email,
                        provider = authStatus.provider,
                        isLoginSuccess = true,
                        needsOnboarding = authStatus.needsOnboarding,
                        error = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Login failed",
                        isLoginSuccess = false
                    )
                }
            }
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Reset login state (after navigation)
     */
    fun resetLoginState() {
        _uiState.update { it.copy(isLoginSuccess = false) }
    }
}
