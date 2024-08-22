package com.example.bookwander.fake

import com.example.bookwander.model.json.Book
import com.example.bookwander.model.json.ImageLinks
import com.example.bookwander.model.json.Items
import com.example.bookwander.model.json.SaleInfo
import com.example.bookwander.model.json.VolumeInfo

object FakeDataSources {

    val fakeBooksTrending = Items(
        listOf(
            Book(
                id = "1",
                volumeInfo = VolumeInfo(
                    title = "Kotlin for Beginners",
                    authors = listOf("John Doe", "Jane Smith"),
                    publisher = "Tech Books Publishing",
                    publishedDate = "2023-01-01",
                    description = "A comprehensive guide to Kotlin programming for beginners.",
                    categories = listOf("Programming", "Kotlin"),
                    imageLinks = ImageLinks(
                        thumbnail = "http://example.com/thumbnail1.jpg"
                    ),
                    pageCount = 350
                ),
                saleInfo = SaleInfo(
                    buyLink = "http://example.com/buyKotlinForBeginners"
                )
            ),
            Book(
                id = "2",
                volumeInfo = VolumeInfo(
                    title = "Advanced Android Development",
                    authors = listOf("Alice Johnson", "Bob Brown"),
                    publisher = "Mobile Dev Publishing",
                    publishedDate = "2022-05-15",
                    description = "Deep dive into advanced topics of Android development using Kotlin.",
                    categories = listOf("Programming", "Android"),
                    imageLinks = ImageLinks(
                        thumbnail = "http://example.com/thumbnail2.jpg"
                    ),
                    pageCount = 500
                ),
                saleInfo = SaleInfo(
                    buyLink = "http://example.com/buyAdvancedAndroidDevelopment"
                )
            ),
            Book(
                id = "3",
                volumeInfo = VolumeInfo(
                    title = "Design Patterns in Kotlin",
                    authors = listOf("Charlie Black"),
                    publisher = "Software Design Publishing",
                    publishedDate = "2021-09-10",
                    description = "An in-depth look at design patterns and how to implement them in Kotlin.",
                    categories = listOf("Programming", "Software Design"),
                    imageLinks = ImageLinks(
                        thumbnail = "http://example.com/thumbnail3.jpg"
                    ),
                    pageCount = 450
                ),
                saleInfo = SaleInfo(
                    buyLink = "http://example.com/buyDesignPatternsInKotlin"
                )
            )
        )
    )

    val fakeBooksCategory = Items(
        listOf(
            Book(
                id = "2",
                volumeInfo = VolumeInfo(
                    title = "Kotlin for Beginners",
                    authors = listOf("John Doe", "Jane Smith"),
                    publisher = "Tech Books Publishing",
                    publishedDate = "2023-01-01",
                    description = "A comprehensive guide to Kotlin programming for beginners.",
                    categories = listOf("Programming", "Kotlin"),
                    imageLinks = ImageLinks(
                        thumbnail = "http://example.com/thumbnail1.jpg"
                    ),
                    pageCount = 350
                ),
                saleInfo = SaleInfo(
                    buyLink = "http://example.com/buyKotlinForBeginners"
                )
            ),
            Book(
                id = "2",
                volumeInfo = VolumeInfo(
                    title = "Advanced Android Development",
                    authors = listOf("Alice Johnson", "Bob Brown"),
                    publisher = "Mobile Dev Publishing",
                    publishedDate = "2022-05-15",
                    description = "Deep dive into advanced topics of Android development using Kotlin.",
                    categories = listOf("Programming", "Android"),
                    imageLinks = ImageLinks(
                        thumbnail = "http://example.com/thumbnail2.jpg"
                    ),
                    pageCount = 500
                ),
                saleInfo = SaleInfo(
                    buyLink = "http://example.com/buyAdvancedAndroidDevelopment"
                )
            ),
            Book(
                id = "3",
                volumeInfo = VolumeInfo(
                    title = "Design Patterns in Kotlin",
                    authors = listOf("Charlie Black"),
                    publisher = "Software Design Publishing",
                    publishedDate = "2021-09-10",
                    description = "An in-depth look at design patterns and how to implement them in Kotlin.",
                    categories = listOf("Programming", "Software Design"),
                    imageLinks = ImageLinks(
                        thumbnail = "http://example.com/thumbnail3.jpg"
                    ),
                    pageCount = 450
                ),
                saleInfo = SaleInfo(
                    buyLink = "http://example.com/buyDesignPatternsInKotlin"
                )
            )
        )
    )



}