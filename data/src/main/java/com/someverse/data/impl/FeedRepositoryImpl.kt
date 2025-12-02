package com.someverse.data.impl

import com.someverse.data.mapper.FeedMapper.toDomain
import com.someverse.data.source.FeedDataSource
import com.someverse.domain.model.Feed
import com.someverse.domain.model.FeedType
import com.someverse.domain.repository.FeedRepository
import javax.inject.Inject

/**
 * Feed Repository Implementation
 * - Implements FeedRepository interface from domain layer
 * - Depends on FeedDataSource interface (not concrete implementation)
 * - Uses Mapper to convert Entity (DTO) to Domain Model
 * - Handles error handling and data transformation
 */
class FeedRepositoryImpl @Inject constructor(
    private val dataSource: FeedDataSource // Interface injection!
) : FeedRepository {

    override suspend fun createFeed(
        feedType: FeedType,
        movieId: Long?,
        musicId: Long?,
        content: String
    ): Result<Feed> {
        return try {
            val feedEntity = dataSource.createFeed(
                feedType = feedType.name,
                movieId = movieId,
                musicId = musicId,
                content = content
            )
            Result.success(feedEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRandomFeeds(): Result<List<Feed>> {
        return try {
            val feedEntities = dataSource.getRandomFeeds()
            Result.success(feedEntities.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyFeeds(): Result<List<Feed>> {
        return try {
            val feedEntities = dataSource.getMyFeeds()
            Result.success(feedEntities.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFeedDetail(feedId: Long): Result<Feed> {
        return try {
            val feedEntity = dataSource.getFeedDetail(feedId)
            Result.success(feedEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFeed(feedId: Long, content: String): Result<Feed> {
        return try {
            val feedEntity = dataSource.updateFeed(feedId, content)
            Result.success(feedEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun inactivateFeed(feedId: Long): Result<Boolean> {
        return try {
            val success = dataSource.inactivateFeed(feedId)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun activateFeed(feedId: Long): Result<Feed> {
        return try {
            val feedEntity = dataSource.activateFeed(feedId)
            Result.success(feedEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun inactivateAllFeeds(): Result<Boolean> {
        return try {
            val success = dataSource.inactivateAllFeeds()
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}