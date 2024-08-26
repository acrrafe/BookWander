package com.example.bookwander.data.repository

import com.example.bookwander.domain.repository.BookWanderRepository
import com.example.bookwander.model.json.Items
import com.example.bookwander.data.remote.BookApiService


// Overriding the function that gets the value from our Api Service function
class BookWanderRepositoryImpl (
    private val bookApiService: BookApiService
): BookWanderRepository {
    override suspend fun searchBook(
        userQuery: String,
        startIndex: Int,
        maxResults: Int,
    ): Items = bookApiService.searchBook(userQuery, startIndex, maxResults)
}