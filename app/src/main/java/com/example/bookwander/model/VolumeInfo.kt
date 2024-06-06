package com.example.bookwander.model

import kotlinx.serialization.Serializable

@Serializable
data class VolumeInfo(
    val title: String = "",
    val authors: List<String> = emptyList(),
    val publisher: String = "",
    val publishedDate: String = "",
    val description: String = "",
    val categories: List<String> = emptyList(),
    val imageLinks: ImageLinks? = null // making it nullable just in case of missing images
)
