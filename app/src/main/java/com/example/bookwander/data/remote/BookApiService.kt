package com.example.bookwander.data.remote

import com.example.bookwander.model.json.Items
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * For this we create a temporary initialization of retrofit and retrofitService
 * to be able to test with view model if we are able to sent a request to a server and get
 * the data from it, but it is not a good practice to access the data from the view model itself
 * as it is purpose is to work with UI state layer.
 * The best practice will be the use of Repository and Dependency Injection for
 * abstraction, maintainability, and testability of my code.
 * **/

//private const val BASE_URL = "https://www.googleapis.com/books/v1/"

//val networkJson = Json { ignoreUnknownKeys = true }
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
//    .baseUrl(BASE_URL)
//    .build()

interface BookApiService {
    @GET("volumes?")
    suspend fun searchBook(
        @Query("q") userQuery:String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResult: Int
    ): Items

//    @GET("volumes/{volumeId}")
//    suspend fun getBookDetail(@Query("volumeId") bookId:String): Book
}

//object BookApi{
//    val retrofitService: BookApiService by lazy {
//        retrofit.create(BookApiService::class.java)
//    }
//}