package com.someverse.presentation.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.someverse.domain.model.MessageType
import com.someverse.domain.usecase.chat.EnterChatRoomUseCase
import com.someverse.domain.usecase.chat.GetMessageHistoryUseCase
import com.someverse.domain.usecase.chat.MarkMessagesAsReadUseCase
import com.someverse.domain.usecase.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Detail Chat ViewModel
 * - Manages chat detail UI state
 * - Handles message history loading with pagination
 * - Handles sending messages
 * - No business logic (delegated to UseCase)
 */
@HiltViewModel
class DetailChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessageHistoryUseCase: GetMessageHistoryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    private val enterChatRoomUseCase: EnterChatRoomUseCase
) : ViewModel() {

    private val roomId: Long = savedStateHandle.get<Long>("roomId") ?: 0L
    private val partnerNickname: String = savedStateHandle.get<String>("partnerNickname") ?: ""

    private val _uiState = MutableStateFlow(
        DetailChatUiState(
            partnerNickname = partnerNickname
        )
    )
    val uiState: StateFlow<DetailChatUiState> = _uiState.asStateFlow()

    init {
        enterChatRoom()
        loadMessageHistory()
    }

    /**
     * Enter chat room
     */
    private fun enterChatRoom() {
        viewModelScope.launch {
            enterChatRoomUseCase(roomId)
                .onSuccess {
                    // Successfully entered
                    markMessagesAsRead()
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(error = exception.message) }
                }
        }
    }

    /**
     * Load message history from repository
     */
    fun loadMessageHistory(page: Int = 0) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getMessageHistoryUseCase(
                roomId = roomId,
                page = page,
                size = 50
            ).onSuccess { history ->
                val currentMessages = if (page == 0) emptyList() else _uiState.value.messages
                val newMessages = currentMessages + history.messages

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        messages = newMessages,
                        currentPage = history.currentPage,
                        hasMore = history.hasNext
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            }
        }
    }

    /**
     * Send a message
     */
    fun sendMessage(content: String) {
        if (content.isBlank() || _uiState.value.isSending) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, error = null) }

            sendMessageUseCase(
                roomId = roomId,
                content = content,
                messageType = MessageType.TEXT
            ).onSuccess { message ->
                // Add the new message to the list
                val updatedMessages = _uiState.value.messages + message
                _uiState.update {
                    it.copy(
                        isSending = false,
                        messages = updatedMessages
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isSending = false,
                        error = exception.message
                    )
                }
            }
        }
    }

    /**
     * Mark all messages as read
     */
    private fun markMessagesAsRead() {
        viewModelScope.launch {
            markMessagesAsReadUseCase(roomId)
                .onFailure { exception ->
                    // Silent fail - not critical for UI
                    println("Failed to mark messages as read: ${exception.message}")
                }
        }
    }

    /**
     * Load more messages (pagination)
     */
    fun loadMoreMessages() {
        if (_uiState.value.hasMore && !_uiState.value.isLoading) {
            loadMessageHistory(_uiState.value.currentPage + 1)
        }
    }

    /**
     * Refresh messages
     */
    fun refresh() {
        loadMessageHistory(0)
    }
}