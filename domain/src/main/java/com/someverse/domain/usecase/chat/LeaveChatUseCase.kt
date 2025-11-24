package com.someverse.domain.usecase.chat

import com.someverse.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Leave Chat Use Case
 * - Single Responsibility: Handle leaving a chat room
 * - Business logic: Validate room ID before leaving
 * - Delegates to ChatRepository
 */
class LeaveChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Leave a chat room
     *
     * @param roomId Chat room ID to leave
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
        return chatRepository.leaveChat(roomId)
    }
}