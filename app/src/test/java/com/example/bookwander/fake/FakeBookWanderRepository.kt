package com.example.bookwander.fake

import com.example.bookwander.data.BookshelfRepository
import com.example.bookwander.model.Items


class FakeBookWanderRepository: BookshelfRepository {
    override suspend fun searchBook(userQuery: String): Items {
        return FakeDataSources.fakeBooks
    }
}