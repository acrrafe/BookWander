package com.example.bookwander.fake

import com.example.bookwander.data.repository.BookWanderRepositoryImpl
import com.example.bookwander.model.Items
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class NetworkBookWanderRepositoryTest {
    @Test
    fun networkBookWanderRepository_getBooks_verifyBookList() =
        runTest {
            val repository = BookWanderRepositoryImpl(
                bookApiService = FakeBookWanderApiService()
            )
            // Success Tests
            assertEquals(FakeDataSources.fakeBooksTrending, repository.searchBook("Trending"))
            assertEquals(FakeDataSources.fakeBooksCategory, repository.searchBook("Entrepreneur"))
            assertEquals(Items(emptyList()), repository.searchBook(""))

            // Incorrect Values
            assertNotEquals(FakeDataSources.fakeBooksTrending, repository.searchBook("Entrepreneur"))
            assertNotEquals(FakeDataSources.fakeBooksCategory, repository.searchBook("Trending"))
            assertNotEquals(Items(emptyList()), repository.searchBook("Trending"))

        }
}