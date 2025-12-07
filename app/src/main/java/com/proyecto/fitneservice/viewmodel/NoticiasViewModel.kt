package com.proyecto.fitneservice.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.fitneservice.data.model.Article
import com.proyecto.fitneservice.data.network.NewsApiService
import kotlinx.coroutines.launch

class NoticiasViewModel : ViewModel() {


    var newsList = mutableStateOf<List<Article>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf("")
        private set

    private val apiService = NewsApiService.create()

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""
            try {

                val apiKey = "9f369c5b479c4cd4aeb341b339d6b7ac"


                val response = apiService.getTopHeadlines(country = "us", category = "health", apiKey = apiKey)
                newsList.value = response.articles
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}