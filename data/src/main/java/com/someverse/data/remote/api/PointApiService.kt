package com.someverse.data.remote.api

import com.someverse.data.model.PointApiResponse
import com.someverse.data.model.PointEntity
import retrofit2.http.GET

/**
 * Point API Service Interface
 * - Defines Retrofit API endpoints for point (루미) functionality
 */
interface PointApiService {

    /**
     * Get point balance
     * GET /points/balance
     *
     * @return Point balance response
     */
    @GET("points/balance")
    suspend fun getPointBalance(): PointApiResponse<PointEntity>
}