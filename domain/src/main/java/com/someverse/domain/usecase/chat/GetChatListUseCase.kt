package com.someverse.domain.usecase.chat

import com.someverse.domain.model.Chat
import com.someverse.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Get Chat List Use Case
 * - Single Responsibility: Retrieve user's chat room list
 * - Business logic: Sort chat list by last message time
 * - Delegates to ChatRepository
 */
class GetChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Get list of user's chat rooms
     *
     * @return Result<List<Chat>> list of chat rooms sorted by last message time
     */
    suspend operator fun invoke(): Result<List<Chat>> {
        return chatRepository.getChatList().map { chatList ->
            // Business logic: Sort by last message time (most recent first)
            chatList.sortedByDescending { chat ->
                chat.lastMessageTime ?: ""
            }
        }
    }
}