package com.someverse.presentation.ui.chat

import com.someverse.domain.model.ChatMessage

/**
 * Detail Chat UI State
 * - Represents the state of chat detail screen
 */
data class DetailChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val partnerNickname: String = "",
    val partnerProfileImage: String? = null,
    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val isSending: Boolean = false,
    val error: String? = null
)