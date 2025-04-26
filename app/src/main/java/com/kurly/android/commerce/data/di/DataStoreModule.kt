package com.kurly.android.commerce.data.di

import android.content.Context
import com.kurly.android.commerce.data.local.FavoritePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideFavoritePreferences(
        @ApplicationContext context: Context
    ): FavoritePreferences {
        return FavoritePreferences(context)
    }
}