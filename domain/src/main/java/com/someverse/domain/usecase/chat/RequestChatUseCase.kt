package com.someverse.domain.usecase.chat

import com.someverse.domain.model.Chat
import com.someverse.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Request Chat Use Case
 * - Single Responsibility: Handle chat request with validation
 * - Business logic: Validate message before requesting chat
 * - Delegates to ChatRepository
 */
class RequestChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Request chat with a user
     *
     * @param userId Target user ID to chat with
     * @param message Initial message to send
     * @return Result<Chat> created chat room or failure with error
     */
    suspend operator fun invoke(userId: Int, message: String): Result<Chat> {
        // Business logic: Validate input
        if (userId <= 0) {
            return Result.failure(
                IllegalArgumentException("Invalid user ID")
            )
        }

        if (message.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Message cannot be blank")
            )
        }

        if (message.length > 500) {
            return Result.failure(
                IllegalArgumentException("Message must be less than 500 characters")
            )
        }

        // Delegate to repository
        return chatRepository.requestChat(userId, message)
    }
}