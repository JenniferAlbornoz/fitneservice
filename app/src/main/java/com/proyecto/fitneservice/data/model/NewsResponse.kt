package com.proyecto.fitneservice.data.model


data class NewsResponse(
    val status: String,
    val articles: List<Article>
)


data class Article(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val url: String,
    val source: Source?
)

data class Source(
    val name: String
)