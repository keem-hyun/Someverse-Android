package com.someverse.data.model

import com.google.gson.annotations.SerializedName

/**
 * Point Balance Entity (DTO)
 * - Data Transfer Object for Point Balance
 * - Maps to API response data field
 */
data class PointEntity(
    @SerializedName("pointBalance")
    val pointBalance: Long
)

/**
 * Point API Response Wrapper
 */
data class PointApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T?
)