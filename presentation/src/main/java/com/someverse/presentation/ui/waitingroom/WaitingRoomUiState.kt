package com.someverse.presentation.ui.waitingroom

import com.someverse.domain.model.Chat

/**
 * Waiting Room UI State
 * - Represents the state of waiting room screen
 */
data class WaitingRoomUiState(
    val isLoading: Boolean = false,
    val chatList: List<Chat> = emptyList(),
    val error: String? = null
)