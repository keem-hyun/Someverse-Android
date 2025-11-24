package com.someverse.domain.usecase.chat

import com.someverse.domain.model.Chat
import com.someverse.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Get Chat Detail Use Case
 * - Single Responsibility: Retrieve specific chat room details
 * - Business logic: Validate room ID before fetching
 * - Delegates to ChatRepository
 */
class GetChatDetailUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Get details of a specific chat room
     *
     * @param roomId Chat room ID to fetch
     * @return Result<Chat> chat room details or failure with error
     */
    suspend operator fun invoke(roomId: Int): Result<Chat> {
        // Business logic: Validate room ID
        if (roomId <= 0) {
            return Result.failure(
                IllegalArgumentException("Invalid room ID")
            )
        }

        // Delegate to repository
        return chatRepository.getChatDetail(roomId)
    }
}