package com.example.bookwander.model

import kotlinx.serialization.Serializable

@Serializable
data class Items(
    val items: List<Book>
)
