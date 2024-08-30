package com.example.bookwander.di

import com.example.bookwander.data.remote.BookCategoryPagingSource
import com.example.bookwander.domain.repository.BookWanderRepository
import javax.inject.Inject

/**
 * We made a Factory Design of our BookPagingSource so
 * we can accept values from user input in our viewModel
 * and update the value we use for fetching list of book in our API
 */
class BookCategoryPagingSourceFactory
@Inject constructor(
    private val bookWanderRepository: BookWanderRepository,
    private val maxResult: Int
){

    fun create(currentBookCategory: String): BookCategoryPagingSource{
        return BookCategoryPagingSource(bookWanderRepository, currentBookCategory, maxResult)
    }
}