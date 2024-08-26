package com.example.bookwander.domain.repository

import com.example.bookwander.model.json.Items

// Abstraction of searchBook Function
interface BookWanderRepository {
    suspend fun searchBook(
        userQuery: String,
        startIndex: Int,
        maxResults: Int
    ): Items
}