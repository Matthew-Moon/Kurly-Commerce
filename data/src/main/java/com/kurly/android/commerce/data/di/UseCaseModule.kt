package com.kurly.android.commerce.data.di

import com.kurly.android.commerce.data.repository.KurlyRepositoryImpl
import com.kurly.android.commerce.data.usecase.GetSectionsPagingUseCase
import com.kurly.android.commerce.domain.repository.KurlyRepository
import com.kurly.android.commerce.domain.usecase.GetSectionProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetSectionProductsUseCase(repository: KurlyRepository): GetSectionProductsUseCase {
        return GetSectionProductsUseCase(repository)
    }

    // 데이터 레이어 유즈케이스 (페이징)
    @Provides
    @Singleton
    fun provideGetSectionsPagingUseCase(repository: KurlyRepositoryImpl): GetSectionsPagingUseCase {
        return GetSectionsPagingUseCase(repository)
    }
}