package com.someverse.data.mapper

import com.someverse.data.model.ChatEntity
import com.someverse.data.model.ChatMessageEntity
import com.someverse.data.model.ChatMessageHistoryEntity
import com.someverse.domain.model.Chat
import com.someverse.domain.model.ChatMessage
import com.someverse.domain.model.ChatMessageHistory
import com.someverse.domain.model.ChatStatus
import com.someverse.domain.model.MessageType
import com.someverse.domain.model.UnreadCount

/**
 * Chat Mapper
 * - Converts between Entity (DTO) and Domain Model
 * - Entity: Data layer (API/DB)
 * - Domain: Business logic layer
 */
object ChatMapper {

    /**
     * Convert ChatEntity to Chat domain model
     */
    fun ChatEntity.toDomain(): Chat {
        return Chat(
            roomId = this.roomId,
            partnerId = this.partnerId,
            partnerNickname = this.partnerNickname,
            partnerProfileImage = this.partnerProfileImage,
            status = when (this.status.uppercase()) {
                "PENDING" -> ChatStatus.PENDING
                "ACTIVE" -> ChatStatus.ACTIVE
                "CLOSED" -> ChatStatus.ClOSED
                else -> ChatStatus.ACTIVE
            },
            lastMessage = this.lastMessage,
            lastMessageTime = this.lastMessageTime,
            unreadCount = this.unreadCount,
            isRequester = this.isRequester,
            lumiUsed = this.lumiUsed,
            isFreeChat = this.isFreeChat
        )
    }

    /**
     * Convert Chat domain model to ChatEntity
     */
    fun Chat.toEntity(): ChatEntity {
        return ChatEntity(
            roomId = this.roomId,
            partnerId = this.partnerId,
            partnerNickname = this.partnerNickname,
            partnerProfileImage = this.partnerProfileImage,
            status = this.status.name,
            lastMessage = this.lastMessage,
            lastMessageTime = this.lastMessageTime,
            unreadCount = this.unreadCount,
            isRequester = this.isRequester,
            lumiUsed = this.lumiUsed,
            isFreeChat = this.isFreeChat
        )
    }

    /**
     * Convert ChatMessageEntity to ChatMessage domain model
     */
    fun ChatMessageEntity.toDomain(): ChatMessage {
        return ChatMessage(
            messageId = this.messageId,
            roomId = this.roomId,
            senderId = this.senderId,
            senderNickname = this.senderNickname,
            content = this.content,
            messageType = when (this.messageType.uppercase()) {
                "TEXT" -> MessageType.TEXT
                else -> MessageType.TEXT
            },
            isRead = this.isRead,
            createdAt = this.createdAt
        )
    }

    /**
     * Convert ChatMessage domain model to ChatMessageEntity
     */
    fun ChatMessage.toEntity(): ChatMessageEntity {
        return ChatMessageEntity(
            messageId = this.messageId,
            roomId = this.roomId,
            senderId = this.senderId,
            senderNickname = this.senderNickname,
            content = this.content,
            messageType = this.messageType.name,
            isRead = this.isRead,
            createdAt = this.createdAt
        )
    }

    /**
     * Convert ChatMessageHistoryEntity to ChatMessageHistory domain model
     */
    fun ChatMessageHistoryEntity.toDomain(): ChatMessageHistory {
        return ChatMessageHistory(
            messages = this.messages.map { it.toDomain() },
            currentPage = this.currentPage,
            totalPages = this.totalPages,
            totalElements = this.totalElements,
            hasNext = this.hasNext
        )
    }

    /**
     * Convert UnreadCount domain model to Int (for Entity)
     */
    fun UnreadCount.toInt(): Int {
        return this.unreadCount
    }

    /**
     * Convert Int to UnreadCount domain model
     */
    fun Int.toUnreadCount(): UnreadCount {
        return UnreadCount(unreadCount = this)
    }
}