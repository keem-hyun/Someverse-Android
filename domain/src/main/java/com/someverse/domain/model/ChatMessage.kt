package com.someverse.domain.model

data class ChatMessage(
    val messageId: Long,
    val roomId: Long,
    val senderId: Int,
    val senderNickname: String,
    val content: String,
    val messageType: MessageType,
    val isRead: Boolean,
    val createdAt: String
)

enum class MessageType {
    TEXT
}

data class ChatMessageHistory(
    val messages: List<ChatMessage>,
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Int,
    val hasNext: Boolean
)

data class UnreadCount(
    val unreadCount: Int
)