package com.example.bookwander.fake

import com.example.bookwander.model.json.Items
import com.example.bookwander.data.remote.BookApiService

class FakeBookWanderApiService: BookApiService {
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

//    override suspend fun getBookDetail(bookId: String): Book {
//        return FakeDataSources.fakeBook
//    }

}