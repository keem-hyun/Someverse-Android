package com.someverse.domain.usecase.chat

import com.someverse.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Get Free Chat Count Use Case
 * - Single Responsibility: Retrieve today's remaining free chat count
 * - Business logic: None (direct delegation)
 * - Delegates to ChatRepository
 */
class GetFreeChatCountUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    /**
     * Get today's remaining free chat count
     *
     * @return Result<Int> remaining free chat count or failure with error
     */
    suspend operator fun invoke(): Result<Int> {
        return chatRepository.getFreeChatCount()
    }
}