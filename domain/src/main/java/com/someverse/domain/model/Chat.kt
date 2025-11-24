package com.someverse.domain.model

data class Chat(
    val roomId: Int,
    val partnerId: Int,
    val partnerNickname: String,
    val partnerProfileImage: String?,
    val status: ChatStatus,
    val lastMessage: String?,
    val lastMessageTime: String?,
    val unreadCount: Int,
    val isRequester: Boolean
)

enum class ChatStatus {
    PENDING,
    ACTIVE,
    ClOSED
}
