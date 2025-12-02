package com.someverse.domain.model

/**
 * Feed Domain Model
 */
data class Feed(
    val id: Long,
    val content: String,
    val nickName: String,
    val profileImage: String?
)

/**
 * Feed Type
 */
enum class FeedType {
    MOVIE,
    MUSIC
}