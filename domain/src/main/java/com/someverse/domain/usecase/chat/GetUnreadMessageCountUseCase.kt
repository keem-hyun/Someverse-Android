package com.someverse.domain.usecase.chat

import com.someverse.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Get Unread Message Count Use Case
 * - Single Responsibility: Retrieve total unread message count
 * - Business logic: None (direct delegation)
 * - Delegates to ChatRepository
 */
class GetUnreadMessageCountUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Get total unread message count across all chat rooms
     *
     * @return Result<Int> total unread message count or failure with error
     */
    suspend operator fun invoke(): Result<Int> {
        return chatRepository.getUnreadMessageCount()
    }
}