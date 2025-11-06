package com.someverse.domain.model

/**
 * Social Login Provider Types
 */
enum class SocialProvider {
    KAKAO;

    companion object {
        fun fromString(provider: String): SocialProvider? {
            return entries.find { it.name.equals(provider, ignoreCase = true) }
        }
    }
}