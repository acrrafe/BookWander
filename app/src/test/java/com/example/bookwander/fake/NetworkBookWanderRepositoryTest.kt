package com.example.bookwander.fake

import com.example.bookwander.data.NetworkBookshelfRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBookWanderRepositoryTest {
    @Test
    fun networkBookWanderRepository_getBooks_verifyBookList() =
        runTest {
            val repository = NetworkBookshelfRepository(
                bookApiService = FakeBookWanderApiService()
            )
            assertEquals(FakeDataSources.fakeBooks, repository.searchBook("Trending"))
        }
}