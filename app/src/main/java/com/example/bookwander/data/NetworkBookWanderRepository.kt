package com.example.bookwander.data

import com.example.bookwander.model.Items
import com.example.bookwander.network.BookApiService

// Abstraction of searchBook Function
interface BookshelfRepository {
    suspend fun searchBook(userQuery: String): Items
}

// Overriding the function that gets the value from our Api Service function
class NetworkBookshelfRepository (
    private val bookApiService: BookApiService): BookshelfRepository{

    override suspend fun searchBook(userQuery: String): Items = bookApiService.searchBook(userQuery)
}