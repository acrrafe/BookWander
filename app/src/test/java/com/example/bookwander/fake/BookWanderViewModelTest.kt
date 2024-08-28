package com.example.bookwander.fake

import com.example.bookwander.rules.TestDispatcherRule
import com.example.bookwander.ui.BookUiState
import com.example.bookwander.ui.BookWanderViewModel
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
            // Run the init block in our viewModel
            bookWanderViewModel.getBooksInformation()
            // Test the value
            assertEquals(
                BookUiState.Success(FakeDataSources.fakeBooksTrending.items,
                FakeDataSources.fakeBooksCategory.items), bookWanderViewModel.bookUiState)
        }
}