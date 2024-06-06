package com.example.bookwander.data

import com.example.bookwander.network.BookApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}

class DefaultContainer: AppContainer{
    // Our Base URL
    private val baseUrl = "https://www.googleapis.com/books/v1/"

    val networkJson = Json { ignoreUnknownKeys = true } // By doing this, we are able to ignore unknown Keys in our JSON
    // Initialization of our retrofit where we set the type of converter and our base url
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()
    // Singleton pattern of initializing BookApiService using retrofit
    private val retrofitService: BookApiService by lazy{
        retrofit.create(BookApiService::class.java)
    }
    // Overriding the value of bookshelfRepository to pass our retrofitService
    override val bookshelfRepository: BookshelfRepository by lazy {
        NetworkBookshelfRepository(retrofitService)
    }

}