package com.someverse.domain.model

/**
 * Gender Enum
 * - Type-safe representation of user gender
 * - Used in onboarding process
 */
enum class Gender {
    MALE,
    FEMALE;

    companion object {
        /**
         * Convert string to Gender enum (case-insensitive)
         * @return Gender enum or null if invalid
         */
        fun fromString(value: String): Gender? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}