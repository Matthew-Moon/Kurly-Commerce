package com.kurly.android.commerce.data.di

import com.kurly.android.commerce.data.repository.KurlyRepositoryImpl
import com.kurly.android.commerce.domain.repository.KurlyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindKurlyRepository(
        kurlyRepositoryImpl: KurlyRepositoryImpl
    ): KurlyRepository
}