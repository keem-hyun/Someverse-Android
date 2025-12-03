package com.someverse.presentation.ui.feed

import com.someverse.domain.model.Feed

/**
 * Feed UI State
 * - Manages UI state for Feed screen
 */
data class FeedUiState(
    val isLoading: Boolean = false,
    val feeds: List<Feed> = emptyList(),
    val error: String? = null
)