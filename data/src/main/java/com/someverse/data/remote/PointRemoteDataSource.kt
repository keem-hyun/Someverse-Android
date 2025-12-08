package com.someverse.data.remote

import com.someverse.data.remote.api.PointApiService
import com.someverse.data.source.PointDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Point Remote DataSource (Real API Implementation)
 * - Implements PointDataSource interface
 * - Handles API communication for point (루미) operations
 */
@Singleton
class PointRemoteDataSource @Inject constructor(
    private val pointApiService: PointApiService
) : PointDataSource {

    override suspend fun getPointBalance(): Long {
        val response = pointApiService.getPointBalance()

        if (response.success && response.data != null) {
            return response.data.pointBalance
        } else {
            throw Exception(response.message)
        }
    }
}