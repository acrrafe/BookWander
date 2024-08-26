package com.example.bookwander.model.json

import kotlinx.serialization.Serializable

@Serializable
data class Items(
    val items: List<Book>
)
