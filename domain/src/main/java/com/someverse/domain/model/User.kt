package com.someverse.domain.model

/**
 * User Model
 */
data class User(
    val id: String,
    val name: String,
    val nickname: String,
    val email: String,
    val birth: String,
    val address: List<String>,
    val profileImageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)