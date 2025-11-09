package com.someverse.data.di

import com.someverse.data.impl.AuthRepositoryImpl
import com.someverse.data.local.AuthLocalDataSource
import com.someverse.data.source.AuthDataSource
import com.someverse.domain.repository.AuthRepository
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

    // ==================== Repositories ====================

    @Provides
    @Singleton
    fun provideAuthRepository(
        dataSource: AuthDataSource  // Interface 주입!
    ): AuthRepository {
        return AuthRepositoryImpl(dataSource)
    }
}
