package com.example.bookwander.presentation

import androidx.annotation.StringRes
import retrofit2.HttpException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bookwander.R
import com.example.bookwander.data.remote.BookPagingSource
import com.example.bookwander.domain.repository.BookWanderRepository
import com.example.bookwander.model.json.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

// Fetching Data from the server's state
sealed interface BookUiState{
    data class Success(val books: List<Book>, val bookCategory: List<Book>): BookUiState
    data class Error(@StringRes val message: Int): BookUiState
    object Loading: BookUiState

}

data class BookCategoryUiState(
    val currentSelectedBook: Book? = null,
    val currentBookCategory: String
)

@HiltViewModel
class BookWanderViewModel @Inject constructor(
    private val  bookPagingSource: BookPagingSource,
    private val bookshelfRepository: BookWanderRepository
): ViewModel(){

    var bookUiState: BookUiState by mutableStateOf(BookUiState.Loading)

    private val _bookCategoryUiState = MutableStateFlow(
        BookCategoryUiState(
            currentBookCategory = DEFAULT_CATEGORY
        )
    )

    val bookCategoryUiState: StateFlow<BookCategoryUiState> = _bookCategoryUiState
    /**
     * Initialize a variable booksTrending that can be used for other function
     * This will enable us to update a specific value in our uiState
     */
    private var booksTrending = emptyList<Book>()

    val bookTrendingFlow: Flow<PagingData<Book>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true
        ),
        pagingSourceFactory = { bookPagingSource }
    ).flow.cachedIn(viewModelScope)

    // Initialize the function of getting the information of the books
    init{
        getBooksInformation()
    }


    fun getBooksInformation() {
        viewModelScope.launch{
            bookUiState = BookUiState.Loading
            bookUiState = try {
                booksTrending = bookshelfRepository.searchBook(TRENDING_CATEGORY, 0, 10).items
                val booksCategory = bookshelfRepository.searchBook(bookCategoryUiState.value.currentBookCategory, 0,10).items
                BookUiState.Success(booksTrending, booksCategory).also { newState->
                    bookUiState = newState
                    _bookCategoryUiState.update {
                        it.copy(currentSelectedBook = booksTrending.firstOrNull())
                    }
                }
            } catch (e: IOException) {
                BookUiState.Error(R.string.network_error)
            } catch (e: HttpException) {
                BookUiState.Error(R.string.server_error)
            }
        }


    }

    fun updateBookCategory(currentBookCategory: String){
        _bookCategoryUiState.update {
            it.copy(currentBookCategory = currentBookCategory)
        }
    }

    fun updateSelectedBook(book: Book){
        _bookCategoryUiState.update {
            it.copy(currentSelectedBook = book)
        }
    }

    fun getBookCategory(currentBookCategory: String){
        viewModelScope.launch {
            bookUiState = BookUiState.Loading
            bookUiState = try {
                val booksCategory = bookshelfRepository.searchBook(currentBookCategory, 0, 10).items
                BookUiState.Success(booksTrending, booksCategory)
            }catch (e: IOException){
                BookUiState.Error(R.string.network_error)
            }catch (e: HttpException){
                BookUiState.Error(R.string.server_error)
            }
        }
    }

    companion object{
        private const val DEFAULT_CATEGORY = "Entrepreneur"
        private const val TRENDING_CATEGORY = "Trending"
    }
}