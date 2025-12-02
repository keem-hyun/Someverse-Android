package com.someverse.data.model

import com.google.gson.annotations.SerializedName

/**
 * Feed Entity (DTO)
 * - Data Transfer Object for Feed
 * - Maps to API response
 * - Will be converted to Domain Model (Feed) by Mapper
 */
data class FeedEntity(
    @SerializedName("id")
    val id: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("profileImages")
    val profileImages: String?
)

/**
 * Create Feed Request DTO
 */
data class CreateFeedRequestDto(
    @SerializedName("feedType")
    val feedType: String,
    @SerializedName("movieId")
    val movieId: Long?,
    @SerializedName("musicId")
    val musicId: Long?,
    @SerializedName("content")
    val content: String
)

/**
 * Update Feed Request DTO
 */
data class UpdateFeedRequestDto(
    @SerializedName("content")
    val content: String
)

/**
 * API Response Wrapper
 */
data class FeedApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
)