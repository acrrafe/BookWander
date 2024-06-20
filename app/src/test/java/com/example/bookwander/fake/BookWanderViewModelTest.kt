package com.example.bookwander.fake

import com.example.bookwander.rules.TestDispatcherRule
import com.example.bookwander.ui.screens.BookUiState
import com.example.bookwander.ui.screens.BookWanderViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookWanderViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun bookWanderViewModel_getBooks_verifyBookUiState() =
        runTest {
            val bookWanderViewModel = BookWanderViewModel(
                bookshelfRepository = FakeBookWanderRepository()
            )
            assertEquals(BookUiState.Success(FakeDataSources.fakeBooks.items, FakeDataSources.fakeBooks.items), bookWanderViewModel.bookUiState)
        }
}