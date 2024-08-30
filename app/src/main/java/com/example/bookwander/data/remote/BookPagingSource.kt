package com.example.bookwander.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookwander.data.repository.BookWanderRepositoryImpl
import com.example.bookwander.domain.repository.BookWanderRepository
import com.example.bookwander.model.json.Book
import com.example.bookwander.model.json.VolumeInfo
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * For this class we use PagingSource from Paging 3 Library
 */
class BookPagingSource @Inject constructor(
    private val networkBookWanderRepositoryImpl: BookWanderRepository,
    private val currentBookCategory: String,
    private val maxResult: Int
): PagingSource<Int, Book>(){

    private var totalItems = 0;
    override fun getRefreshKey(state: PagingState<Int, Book>): Int? = state.anchorPosition

    /**
     * This load function is responsible for handling the logic our pagination
     * We make rename the page to startIndex as following the api of books
     * the startIndex is responsible for the new index position for each page
     * The loadSize variable or also the limit variable is responsible for the total items per page
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val startIndex = params.key ?: 0
        val loadSize = params.loadSize

        /**
         * Since the maximum result we can get from our api is 40
         * we're checking if the loaded items is already equal or above the maxResult
         * if it is, then we'll gonna set the list to empty to prevent loading null items
         */
        if (totalItems >= maxResult) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = if (startIndex == 0) null else startIndex - loadSize,
                nextKey = null
            )
        }
        return try {
            // Here we fetched the type of books and pass other parameters
            val response = networkBookWanderRepositoryImpl.searchBook(
                currentBookCategory, startIndex, loadSize
            )
            // Get the Books from Items
            val books = response.items
            // Add the additional loaded items in our totalItems
            totalItems += books.size
            /**
             * Here we pass the books to our LoadResult.Page
             * We set the minus and plus for previous and next key to 13
             * to avoid the duplication of assigning id to keys in our lazyRow
             */
            LoadResult.Page(
                data = books,
                prevKey = if (startIndex == 0) null else startIndex.minus(13),
                nextKey = if (books.isEmpty()) null else startIndex.plus(13)
            )
        } catch (e: IOException) {
            LoadResult.Error(
                e
            )
        } catch (e: HttpException) {
            LoadResult.Error(
                e
            )
        }
    }

}