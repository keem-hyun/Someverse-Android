package com.someverse.data.model

/**
 * Chat Entity (DTO)
 * - Data Transfer Object for Chat
 * - Maps to API response/request
 * - Will be converted to Domain Model (Chat) by Mapper
 */
data class ChatEntity(
    val roomId: Int,
    val partnerId: Int,
    val partnerNickname: String,
    val partnerProfileImage: String?,
    val status: String,
    val lastMessage: String?,
    val lastMessageTime: String?,
    val unreadCount: Int,
    val isRequester: Boolean,
    val lumiUsed: Int,
    val isFreeChat: Boolean
)