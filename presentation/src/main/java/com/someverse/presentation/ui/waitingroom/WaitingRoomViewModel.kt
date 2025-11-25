package com.someverse.presentation.ui.waitingroom

import androidx.lifecycle.ViewModel
import com.someverse.domain.model.Chat
import com.someverse.domain.model.ChatStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Waiting Room ViewModel
 * - Manages waiting room screen state
 */
@HiltViewModel
class WaitingRoomViewModel @Inject constructor(
    // TODO: Inject UseCases when available
) : ViewModel() {

    private val _uiState = MutableStateFlow(WaitingRoomUiState())
    val uiState: StateFlow<WaitingRoomUiState> = _uiState.asStateFlow()

    init {
        loadWaitingRoomChats()
    }

    private fun loadWaitingRoomChats() {
        // TODO: Replace with actual UseCase call
        _uiState.value = WaitingRoomUiState(
            isLoading = false,
            chatList = getDummyWaitingRoomChats()
        )
    }

    // Temporary dummy data
    private fun getDummyWaitingRoomChats(): List<Chat> {
        return listOf(
            Chat(
                roomId = 1,
                partnerId = 101,
                partnerNickname = "마포구보안관2",
                partnerProfileImage = null,
                status = ChatStatus.PENDING,
                lastMessage = "동해물과 백두산이 마르고 닳도록 하느님이...",
                lastMessageTime = "2025-11-24T15:30:00",
                unreadCount = 1,
                isRequester = false
            ),
            Chat(
                roomId = 2,
                partnerId = 102,
                partnerNickname = "마포구보안관2",
                partnerProfileImage = null,
                status = ChatStatus.PENDING,
                lastMessage = "동해물과 백두산이 마르고 닳도록 하느님이...",
                lastMessageTime = "2025-11-24T15:28:00",
                unreadCount = 1,
                isRequester = false
            ),
            Chat(
                roomId = 3,
                partnerId = 103,
                partnerNickname = "마포구보안관2",
                partnerProfileImage = null,
                status = ChatStatus.PENDING,
                lastMessage = "동해물과 백두산이 마르고 닳도록 하느님이...",
                lastMessageTime = "2025-11-24T15:26:00",
                unreadCount = 1,
                isRequester = false
            ),
            Chat(
                roomId = 4,
                partnerId = 104,
                partnerNickname = "마포구보안관2",
                partnerProfileImage = null,
                status = ChatStatus.PENDING,
                lastMessage = "동해물과 백두산이 마르고 닳도록 하느님이...",
                lastMessageTime = "2025-11-24T15:24:00",
                unreadCount = 1,
                isRequester = false
            ),
            Chat(
                roomId = 5,
                partnerId = 105,
                partnerNickname = "마포구보안관2",
                partnerProfileImage = null,
                status = ChatStatus.PENDING,
                lastMessage = "동해물과 백두산이 마르고 닳도록 하느님이...",
                lastMessageTime = "2025-11-24T15:22:00",
                unreadCount = 1,
                isRequester = false
            ),
            Chat(
                roomId = 6,
                partnerId = 106,
                partnerNickname = "마포구보안관2",
                partnerProfileImage = null,
                status = ChatStatus.PENDING,
                lastMessage = "동해물과 백두산이 마르고 닳도록 하느님이...",
                lastMessageTime = "2025-11-24T15:20:00",
                unreadCount = 1,
                isRequester = false
            ),
            Chat(
                roomId = 7,
                partnerId = 107,
                partnerNickname = "마포구보안관2",
                partnerProfileImage = null,
                status = ChatStatus.PENDING,
                lastMessage = "동해물과 백두산이 마르고 닳도록 하느님이...",
                lastMessageTime = "2025-11-24T15:18:00",
                unreadCount = 1,
                isRequester = false
            )
        )
    }
}