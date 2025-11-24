package com.someverse.domain.usecase.chat

import com.someverse.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Delete Chat Use Case
 * - Single Responsibility: Handle deleting a chat room
 * - Business logic: Validate room ID before deletion
 * - Delegates to ChatRepository
 */
class DeleteChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Delete a chat room
     *
     * @param roomId Chat room ID to delete
     * @return Result<Boolean> success status or failure with error
     */
    suspend operator fun invoke(roomId: Int): Result<Boolean> {
        // Business logic: Validate room ID
        if (roomId <= 0) {
            return Result.failure(
                IllegalArgumentException("Invalid room ID")
            )
        }

        // Delegate to repository
        return chatRepository.deleteChat(roomId)
    }
}