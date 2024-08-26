package com.example.bookwander.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bookwander.data.repository.BookWanderRepositoryImpl
import com.example.bookwander.domain.repository.BookWanderRepository
import com.example.bookwander.model.json.Book
import com.example.bookwander.model.json.VolumeInfo
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BookPagingSource @Inject constructor(
    private val networkBookWanderRepositoryImpl: BookWanderRepository,
    private val maxResult: Int
): PagingSource<Int, Book>(){

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val startIndex = params.key ?: 0
        val loadSize = params.loadSize.coerceAtMost(maxResult)
        val response = networkBookWanderRepositoryImpl.searchBook(
            "Entrepreneur", startIndex, loadSize
        )
        val items = response.items
        return try {
            LoadResult.Page(
                data = items,
                prevKey = if (startIndex == 0) null else startIndex.minus(1),
                nextKey = if (items.isEmpty()) null else startIndex.plus(1)
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