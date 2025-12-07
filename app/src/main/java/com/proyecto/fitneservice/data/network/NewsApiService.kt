package com.proyecto.fitneservice.data.network

import com.proyecto.fitneservice.data.model.NewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    // Endpoint para obtener titulares
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us", // Puedes cambiar a "mx", "co", "ar", etc.
        @Query("category") category: String = "health", // Categoría salud/fitness
        @Query("apiKey") apiKey: String
    ): NewsResponse

    companion object {
        // 🔹 URL Base de NewsAPI
        private const val BASE_URL = "https://newsapi.org/"

        fun create(): NewsApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApiService::class.java)
        }
    }
}