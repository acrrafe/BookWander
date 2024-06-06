package com.example.bookwander.model

import androidx.annotation.StringRes
import com.example.bookwander.R

enum class Screen (@StringRes val route: Int) {
    Start(R.string.app_name),
    Book(R.string.book_details)
}