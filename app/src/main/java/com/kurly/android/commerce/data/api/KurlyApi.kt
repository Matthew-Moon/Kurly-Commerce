package com.kurly.android.commerce.data.api

import com.kurly.android.commerce.data.model.SectionListResponse
import com.kurly.android.commerce.data.model.SectionProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KurlyApi {
    @GET("sections")
    suspend fun getSections(@Query("page") page: Int): SectionListResponse

    @GET("section/products")
    suspend fun getSectionProducts(@Query("sectionId") sectionId: Int): SectionProductsResponse
} 