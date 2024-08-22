package com.example.bookwander.domain.repository

import com.example.bookwander.model.Items

// Abstraction of searchBook Function
interface BookWanderRepository {
    suspend fun searchBook(userQuery: String): Items
}