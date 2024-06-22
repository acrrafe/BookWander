package com.example.bookwander.fake

import com.example.bookwander.data.BookshelfRepository
import com.example.bookwander.model.Items


class FakeBookWanderRepository: BookshelfRepository {
    override suspend fun searchBook(userQuery: String): Items {
        return when (userQuery) {
            "Trending" -> {
                FakeDataSources.fakeBooksTrending
            }
            "Entrepreneur" -> {
                FakeDataSources.fakeBooksCategory
            }
            else -> {
                Items(emptyList())
            }
        }
    }
}