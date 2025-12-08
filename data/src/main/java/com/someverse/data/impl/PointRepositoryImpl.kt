package com.someverse.data.impl

import com.someverse.data.source.PointDataSource
import com.someverse.domain.repository.PointRepository
import javax.inject.Inject

/**
 * Point Repository Implementation
 * - Implements PointRepository interface from domain layer
 * - Depends on PointDataSource interface (not concrete implementation)
 * - Handles error handling and data transformation
 */
class PointRepositoryImpl @Inject constructor(
    private val dataSource: PointDataSource // Interface injection!
) : PointRepository {

    override suspend fun getPointBalance(): Long {
        return dataSource.getPointBalance()
    }
}