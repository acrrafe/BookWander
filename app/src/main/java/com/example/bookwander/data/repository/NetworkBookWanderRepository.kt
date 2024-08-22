package com.example.bookwander.data.repository

import com.example.bookwander.domain.repository.BookWanderRepository
import com.example.bookwander.model.Items
import com.example.bookwander.network.BookApiService


// Overriding the function that gets the value from our Api Service function
class BookWanderRepositoryImpl (
    private val bookApiService: BookApiService): BookWanderRepository {
    override suspend fun searchBook(userQuery: String): Items = bookApiService.searchBook(userQuery)
}