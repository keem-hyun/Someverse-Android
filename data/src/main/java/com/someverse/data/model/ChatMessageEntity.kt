package com.someverse.data.model

/**
 * Chat Message Entity (DTO)
 * - Data Transfer Object for ChatMessage
 * - Maps to API response/request
 * - Will be converted to Domain Model (ChatMessage) by Mapper
 */
data class ChatMessageEntity(
    val messageId: Long,
    val roomId: Long,
    val senderId: Int,
    val senderNickname: String,
    val content: String,
    val messageType: String,
    val isRead: Boolean,
    val createdAt: String
)

/**
 * Chat Message History Entity (DTO)
 * - Paginated message history response
 */
data class ChatMessageHistoryEntity(
    val messages: List<ChatMessageEntity>,
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Int,
    val hasNext: Boolean
)