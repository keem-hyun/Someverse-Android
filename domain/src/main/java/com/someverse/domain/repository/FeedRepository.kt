package com.someverse.domain.repository

import com.someverse.domain.model.Feed
import com.someverse.domain.model.FeedType

/**
 * Feed Repository Interface
 * - Handles feed related operations
 * - Implementation will be in data layer
 * - Use Result<T> for error handling
 */
interface FeedRepository {

    /**
     * Create feed
     * POST /feed/created
     *
     * @param feedType Type of feed (MOVIE or MUSIC)
     * @param movieId Movie ID (required if feedType is MOVIE)
     * @param musicId Music ID (required if feedType is MUSIC)
     * @param content Feed content
     * @return Created Feed
     */
    suspend fun createFeed(
        feedType: FeedType,
        movieId: Long?,
        musicId: Long?,
        content: String
    ): Result<Feed>

    /**
     * Get random feeds
     * GET /feed/random
     *
     * @return List of random feeds
     */
    suspend fun getRandomFeeds(): Result<List<Feed>>

    /**
     * Get my feeds
     * GET /feed/my
     *
     * @return List of all my feeds
     */
    suspend fun getMyFeeds(): Result<List<Feed>>

    /**
     * Get feed detail by id
     * GET /feed/{feedId}
     *
     * @param feedId Feed ID
     * @return Feed detail
     */
    suspend fun getFeedDetail(feedId: Long): Result<Feed>

    /**
     * Update feed
     * PUT /feed/{feedId}/update
     *
     * @param feedId Feed ID to update
     * @param content Updated content
     * @return Updated Feed
     */
    suspend fun updateFeed(feedId: Long, content: String): Result<Feed>

    /**
     * Inactivate feed
     * DELETE /feed/{feedId}/inActive
     *
     * @param feedId Feed ID to inactivate
     * @return Boolean indicating success
     */
    suspend fun inactivateFeed(feedId: Long): Result<Boolean>

    /**
     * Activate feed
     * PUT /feed/{feedId}/active
     *
     * @param feedId Feed ID to activate
     * @return Activated Feed
     */
    suspend fun activateFeed(feedId: Long): Result<Feed>

    /**
     * Inactivate all feeds (when user withdraws)
     * DELETE /feed/all/withDraw
     *
     * @return Boolean indicating success
     */
    suspend fun inactivateAllFeeds(): Result<Boolean>
}