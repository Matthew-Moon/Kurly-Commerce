package com.kurly.android.commerce.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")
        private fun getFavoriteKey(productId: Long) = booleanPreferencesKey("favorite_$productId")
    }

    suspend fun setFavorite(productId: Long, isFavorite: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[getFavoriteKey(productId)] = isFavorite
        }
    }

    // 즉시 값을 반환하는 메서드 추가 (블로킹 호출)
    fun isFavorite(productId: Long): Boolean {
        return runBlocking {
            context.dataStore.data.first()[getFavoriteKey(productId)] ?: false
        }
    }
} 