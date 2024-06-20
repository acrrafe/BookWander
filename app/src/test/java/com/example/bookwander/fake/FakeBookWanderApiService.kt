package com.example.bookwander.fake

import com.example.bookwander.model.Book
import com.example.bookwander.model.Items
import com.example.bookwander.network.BookApiService

class FakeBookWanderApiService: BookApiService{
    override suspend fun searchBook(userQuery: String): Items {
        return FakeDataSources.fakeBooks
    }

//    override suspend fun getBookDetail(bookId: String): Book {
//        return FakeDataSources.fakeBook
//    }

}