package com.someverse.data.di

import com.someverse.data.impl.AuthRepositoryImpl
import com.someverse.data.impl.ChatRepositoryImpl
import com.someverse.data.impl.FeedRepositoryImpl
import com.someverse.data.impl.PointRepositoryImpl
import com.someverse.data.local.AuthLocalDataSource
import com.someverse.data.local.ChatLocalDataSource
import com.someverse.data.local.FeedLocalDataSource
import com.someverse.data.local.PointLocalDataSource
import com.someverse.data.source.AuthDataSource
import com.someverse.data.source.ChatDataSource
import com.someverse.data.source.FeedDataSource
import com.someverse.data.source.PointDataSource
import com.someverse.domain.repository.AuthRepository
import com.someverse.domain.repository.ChatRepository
import com.someverse.domain.repository.FeedRepository
import com.someverse.domain.repository.PointRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // ==================== DataSources ====================

    /**
     * Provide AuthDataSource implementation
     *
     * SWITCH BETWEEN LOCAL AND REMOTE HERE:
     * - Local (Mock): return AuthLocalDataSource()
     * - Remote (API): return AuthRemoteDataSource(provideAuthApiService(provideRetrofit(...)))
     */
    @Provides
    @Singleton
    fun provideAuthDataSource(): AuthDataSource {
        // 현재: Local (Mock) 사용
        return AuthLocalDataSource()

        // 나중에 Remote로 교체:
        // return AuthRemoteDataSource(authApiService)
    }

    /**
     * Provide ChatDataSource implementation
     *
     * SWITCH BETWEEN LOCAL AND REMOTE HERE:
     * - Local (Mock): return ChatLocalDataSource()
     * - Remote (API): return ChatRemoteDataSource(chatApiService)
     */
    @Provides
    @Singleton
    fun provideChatDataSource(): ChatDataSource {
        // 현재: Local (Mock) 사용
        return ChatLocalDataSource()

        // 나중에 Remote로 교체:
        // return ChatRemoteDataSource(chatApiService)
    }

    /**
     * Provide FeedDataSource implementation
     *
     * SWITCH BETWEEN LOCAL AND REMOTE HERE:
     * - Local (Mock): return FeedLocalDataSource()
     * - Remote (API): return FeedRemoteDataSource(feedApiService)
     */
    @Provides
    @Singleton
    fun provideFeedDataSource(): FeedDataSource {
        // 현재: Local (Mock) 사용
        return FeedLocalDataSource()

        // 나중에 Remote로 교체:
        // return FeedRemoteDataSource(feedApiService)
    }

    /**
     * Provide PointDataSource implementation
     *
     * SWITCH BETWEEN LOCAL AND REMOTE HERE:
     * - Local (Mock): return PointLocalDataSource()
     * - Remote (API): return PointRemoteDataSource(pointApiService)
     */
    @Provides
    @Singleton
    fun providePointDataSource(): PointDataSource {
        // 현재: Local (Mock) 사용
        return PointLocalDataSource()

        // 나중에 Remote로 교체:
        // return PointRemoteDataSource(pointApiService)
    }

    // ==================== Repositories ====================

    @Provides
    @Singleton
    fun provideAuthRepository(
        dataSource: AuthDataSource  // Interface 주입!
    ): AuthRepository {
        return AuthRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        dataSource: ChatDataSource  // Interface 주입!
    ): ChatRepository {
        return ChatRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideFeedRepository(
        dataSource: FeedDataSource  // Interface 주입!
    ): FeedRepository {
        return FeedRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun providePointRepository(
        dataSource: PointDataSource  // Interface 주입!
    ): PointRepository {
        return PointRepositoryImpl(dataSource)
    }
}