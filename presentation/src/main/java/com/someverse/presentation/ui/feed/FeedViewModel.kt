package com.someverse.presentation.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.someverse.domain.usecase.feed.GetRandomFeedsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Feed ViewModel
 * - Manages feed UI state
 * - Handles feed loading
 * - No business logic (delegated to UseCase)
 */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getRandomFeedsUseCase: GetRandomFeedsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadFeeds()
    }

    /**
     * Load random feeds from repository
     */
    fun loadFeeds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getRandomFeedsUseCase()
                .onSuccess { feeds ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            feeds = feeds,
                            error = null
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                }
        }
    }

    /**
     * Refresh feeds
     */
    fun refresh() {
        loadFeeds()
    }
}