package com.someverse.domain.model

/**
 * Social Login Provider Types
 */
enum class SocialProvider {
    KAKAO,
    GOOGLE,
    NAVER,
    UNKNOWN; // Default fallback

    companion object {
        /**
         * Safe conversion from string to SocialProvider
         * Returns UNKNOWN if provider string is invalid or null
         */
        fun fromString(provider: String?): SocialProvider {
            if (provider.isNullOrBlank()) return UNKNOWN
            return entries.find { it.name.equals(provider, ignoreCase = true) } ?: UNKNOWN
        }
    }
}