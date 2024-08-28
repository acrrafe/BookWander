package com.example.bookwander.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.bookwander.data.repository.BookWanderRepositoryImpl
import com.example.bookwander.domain.repository.BookWanderRepository
import com.example.bookwander.data.remote.BookApiService
import com.example.bookwander.data.remote.BookPagingSource
import com.example.bookwander.model.json.Book
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

/**
 * This are the module for our dependencies
 * It will make sure that our dependency will live as long as
 * the application lives.
 * There are other annotation if we want to change the lifecycle of this module
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // By doing this, we are able to ignore unknown Keys in our JSON
    private val networkJson = Json { ignoreUnknownKeys = true }

    /**
     * These functions will be use by the dagger to initialize the values for these dependencies
     * whenever we inject this in the constructor of a class.
     *
     * @Singleton will make sure that there's only one instance of our BookApiService and
     * BookRepository whenever we inject these dependencies on other classes constructor
     */

    @Provides
    @Singleton
    fun provideBookApiService(): BookApiService {
        return Retrofit.Builder()
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://www.googleapis.com/books/v1/")
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideBookWanderRepository(bookApiService: BookApiService): BookWanderRepository {
        return BookWanderRepositoryImpl(bookApiService)
    }

    @Provides
    fun maxResults(): Int = 40

    @Provides
    fun provideBookPagingSource(bookPagingSource: BookPagingSource): Pager<Int, Book> {
       return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 40,
                prefetchDistance = 5,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { bookPagingSource }
       )
    }
}