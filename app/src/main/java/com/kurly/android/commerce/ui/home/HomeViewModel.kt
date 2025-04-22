package com.kurly.android.commerce.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurly.android.commerce.data.repository.KurlyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KurlyRepository
) : ViewModel() {

    fun loadSections() {
        viewModelScope.launch {
            try {
                val getSections = repository.getSections(1)
                Timber.d("### getSections ==> ${getSections}")

                val getSectionProducts = repository.getSectionProducts(1)
                Timber.d("### getSectionProducts ==> ${getSectionProducts}")
            } catch (e: Exception) {
                Timber.e("### 에러 발생: ${e.message}")
            }
        }
    }
}