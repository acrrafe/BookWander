package com.example.bookwander.ui.screens

import androidx.annotation.StringRes
import retrofit2.HttpException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookwander.BookWanderApplication.BookWanderApplication
import com.example.bookwander.R
import com.example.bookwander.data.BookshelfRepository
import com.example.bookwander.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch

import java.io.IOException

/**
 *  TODO Figure out how to play with the values we need to pass to change the category of books in MVVM
 *  TODO Study how sealed interface works with UiState in ViewModel
 */


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

class BookWanderViewModel(private val bookshelfRepository: BookshelfRepository): ViewModel(){

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

    // Initialize the function of getting the information of the books
    init{
        getBooksInformation()
    }


    private fun getBooksInformation() {
        viewModelScope.launch{
            bookUiState = BookUiState.Loading
            bookUiState = try {
                booksTrending = bookshelfRepository.searchBook(TRENDING_CATEGORY).items
                val booksCategory = bookshelfRepository.searchBook(bookCategoryUiState.value.currentBookCategory).items
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
                val booksCategory = bookshelfRepository.searchBook(currentBookCategory).items
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
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =(this[APPLICATION_KEY] as BookWanderApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookWanderViewModel(bookshelfRepository = bookshelfRepository)
            }
        }
    }


}