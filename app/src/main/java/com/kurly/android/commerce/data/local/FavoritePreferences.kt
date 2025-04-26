package com.kurly.android.commerce.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritePreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")
        private fun getFavoriteKey(productId: Long) = booleanPreferencesKey("favorite_$productId")
    }

    suspend fun setFavorite(productId: Long, isFavorite: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[getFavoriteKey(productId)] = isFavorite
        }
    }

    fun getFavoriteStatus(productId: Long): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[getFavoriteKey(productId)] == true
        }
    }
} 